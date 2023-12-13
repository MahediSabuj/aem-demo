/*******************************************************************************
 * Copyright 2019 Adobe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
(function(window, $, channel, Granite, Coral) {
  "use strict";

  // class of the edit dialog content
  var CLASS_EDIT_DIALOG = "cmp-contentfragment__editor";

  // field selectors
  var SELECTOR_FRAGMENT_PATH = "[name='./fragmentPath']";
  var SELECTOR_VARIATION_NAME = "[name='./variationName']";

  // ui helper
  var ui = $(window).adaptTo("foundation-ui");

  // dialog texts
  var confirmationDialogTitle = Granite.I18n.get("Warning");
  var confirmationDialogMessage = Granite.I18n.get("Please confirm replacing the current content fragment and its configuration");
  var confirmationDialogCancel = Granite.I18n.get("Cancel");
  var confirmationDialogConfirm = Granite.I18n.get("Confirm");
  var errorDialogTitle = Granite.I18n.get("Error");
  var errorDialogMessage = Granite.I18n.get("Failed to load the elements of the selected content fragment");

  // the fragment path field (foundation autocomplete)
  var fragmentPath;

  // keeps track of the current fragment path
  var currentFragmentPath;

  var editDialog;
  var elementsController;

  /**
   * A class which encapsulates the logic related to element selectors and variation name selector.
   */
  var ElementsController = function() {
    this.fetchedState = null;
    this._updateFields();
  };

  /**
   * Updates the member fields of this class according to current dom of dialog.
   */
  ElementsController.prototype._updateFields = function() {
    this.variationName = editDialog.querySelector(SELECTOR_VARIATION_NAME);
    this.variationNamePath = this.variationName.dataset.fieldPath;
  };

  /**
   * Disable all the fields of this controller.
   */
  ElementsController.prototype.disableFields = function() {
    if (this.variationName) {
      this.variationName.setAttribute("disabled", "");
    }
  };

  /**
   * Enable all the fields of this controller.
   */
  ElementsController.prototype.enableFields = function() {
    if (this.variationName) {
      this.variationName.removeAttribute("disabled");
    }
  };

  /**
   * Resets all the fields of this controller.
   */
  ElementsController.prototype.resetFields = function() {
    if (this.variationName) {
      this.variationName.value = "";
    }
  };

  /**
   * Creates an http request object for retrieving fragment's element names or variation names and returns it.
   *
   * @param {String} type - type of request. It can have the following values -
   * 1. "variation" for getting variation names
   * 2. "elements" for getting element names
   * @returns {Object} the resulting request
   */
  // ElementsController.prototype.prepareRequest = function(displayMode, type) {
  ElementsController.prototype.prepareRequest = function(type) {
    var data = {
      fragmentPath: fragmentPath.value
    };
    var url;
    if (type === "variation") {
      url = Granite.HTTP.externalize(this.variationNamePath) + ".html";
    }

    return $.get({
      url: url,
      data: data
    });
  };

  /**
   * Retrieves the html for element names and variation names and keeps the fetched values as "fetchedState" member.
   *
   * @param {Function} callback - function to execute when response is received
   */
  ElementsController.prototype.testGetHTML = function(callback) {
    var variationNameRequest = this.prepareRequest("variation");
    var self = this;

    $.when(variationNameRequest).then(function(result) {
      var newVariationName = $(result).find(SELECTOR_VARIATION_NAME)[0];

      Coral.commons.ready(newVariationName, function() {
        self.fetchedState = {
          variationName: newVariationName,
          variationNameHTML: result
        };
        callback();
      });
    }, function() {
      // display error dialog if one of the requests failed
      ui.prompt(errorDialogTitle, errorDialogMessage, "error");
    });
  };

  /**
   * Checks if the current states of element names, single text selector and variation names match with those
   * present in fetchedState.
   *
   * @returns {Boolean} true if the states match or if there was no current state, false otherwise
   */
  ElementsController.prototype.testStateForUpdate = function() {
    if (this.variationName.value && this.variationName.value !== "master") {
      // if we're unsetting the current fragment we need to reset the config
      if (!this.fetchedState || !this.fetchedState.variationName) {
        return false;
      }
      // compare the items of the current and new variation name fields
      if (!itemsAreEqual(this.variationName.items.getAll(), this.fetchedState.variationName.items.getAll())) {
        return false;
      }
    }

    return true;
  };

  /**
   * Replace the current state with the values present in fetchedState and discard the fetchedState thereafter.
   */
  ElementsController.prototype.saveFetchedState = function() {
    if (!this.fetchedState) {
      return;
    }
    this._updateVariationDOM(this.fetchedState.variationName);
    this.discardFetchedState();
  };

  /**
   * Discard the fetchedState data.
   */
  ElementsController.prototype.discardFetchedState = function() {
    this.fetchedState = null;
  };

  /**
   * Updates dom of variation name select dropdown.
   *
   * @param {HTMLElement} dom - dom for variation name dropdown
   */
  ElementsController.prototype._updateVariationDOM = function(dom) {
    // replace the variation name select, keeping its value
    dom.value = this.variationName.value;
    this.variationName.parentNode.replaceChild(dom, this.variationName);
    this.variationName = dom;
    this.variationName.removeAttribute("disabled");
    this._updateFields();
  };

  function initialize(dialog) {
    // get path of component being edited
    editDialog = dialog;

    // get the fields
    fragmentPath = dialog.querySelector(SELECTOR_FRAGMENT_PATH);

    // initialize state variables
    currentFragmentPath = fragmentPath.value;
    elementsController = new ElementsController();

    // disable add button and variation name if no content fragment is currently set
    if (!currentFragmentPath) {
      elementsController.disableFields();
    }

    // register change listener
    $(fragmentPath).off("foundation-field-change").on("foundation-field-change", onFragmentPathChange);
  }

  /**
   * Executes after the fragment path has changed. Shows a confirmation dialog to the user if the current
   * configuration is to be reset and updates the fields to reflect the newly selected content fragment.
   */
  function onFragmentPathChange() {
    // if the fragment was reset (i.e. the fragment path was deleted)
    if (!fragmentPath.value) {
      var canKeepConfig = elementsController.testStateForUpdate();
      if (canKeepConfig) {
        // There was no current configuration. We just need to disable fields.
        currentFragmentPath = fragmentPath.value;
        elementsController.disableFields();
        return;
      }
      // There was some current configuration. Show a confirmation dialog
      confirmFragmentChange(null, null, elementsController.disableFields, elementsController);
      // don't do anything else
      return;
    }

    elementsController.testGetHTML(function() {
      // check if we can keep the current configuration, in which case no confirmation dialog is necessary
      var canKeepConfig = elementsController.testStateForUpdate();
      if (canKeepConfig) {
        if (!currentFragmentPath) {
          elementsController.enableFields();
        }
        currentFragmentPath = fragmentPath.value;
        // its okay to save fetched state
        elementsController.saveFetchedState();
        return;
      }
      // else show a confirmation dialog
      confirmFragmentChange(elementsController.discardFetchedState, elementsController,
          elementsController.saveFetchedState, elementsController);
    });

  }

  /**
   * Presents the user with a confirmation dialog if the current configuration needs to be reset as a result
   * of the content fragment change.
   *
   * @param {Function} cancelCallback - callback to call if change is cancelled
   * @param {Object} cancelCallbackScope - scope (value of "this" keyword) for cancelCallback
   * @param {Function} confirmCallback a callback to execute after the change is confirmed
   * @param {Object} confirmCallbackScope - the scope (value of "this" keyword) to use for confirmCallback
   */
  function confirmFragmentChange(cancelCallback, cancelCallbackScope, confirmCallback, confirmCallbackScope) {

    ui.prompt(confirmationDialogTitle, confirmationDialogMessage, "warning", [{
      text: confirmationDialogCancel,
      handler: function() {
        // reset the fragment path to its previous value
        requestAnimationFrame(function() {
          fragmentPath.value = currentFragmentPath;
        });
        if (cancelCallback) {
          cancelCallback.call(cancelCallbackScope);
        }
      }
    }, {
      text: confirmationDialogConfirm,
      primary: true,
      handler: function() {
        // reset the current configuration
        elementsController.resetFields();
        // update the current fragment path
        currentFragmentPath = fragmentPath.value;
        // execute callback
        if (confirmCallback) {
          confirmCallback.call(confirmCallbackScope);
        }
      }
    }]);
  }

  /**
   * Compares two arrays containing select items, returning true if the arrays have the same size and all contained
   * items have the same value and label.
   *
   * @param {Array} a1 - first array to compare
   * @param {Array} a2 - second array to compare
   * @returns {Boolean} true if both arrays are equal, false otherwise
   */
  function itemsAreEqual(a1, a2) {
    // verify that the arrays have the same length
    if (a1.length !== a2.length) {
      return false;
    }
    for (var i = 0; i < a1.length; i++) {
      var item1 = a1[i];
      var item2 = a2[i];
      if (item1.attributes.value.value !== item2.attributes.value.value ||
          item1.textContent !== item2.textContent) {
        // the values or labels of the current items didn't match
        return false;
      }
    }
    return true;
  }

  /**
   * Initializes the dialog after it has loaded.
   */
  channel.on("foundation-contentloaded", function(e) {
    if (e.target.getElementsByClassName(CLASS_EDIT_DIALOG).length > 0) {
      Coral.commons.ready(e.target, function(dialog) {
        initialize(dialog);
      });
    }
  });

})(window, jQuery, jQuery(document), Granite, Coral);
