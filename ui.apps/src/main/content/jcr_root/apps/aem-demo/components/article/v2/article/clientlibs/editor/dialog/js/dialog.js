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

  var CLASS_EDIT_DIALOG = "cmp-contentfragment__editor";
  var SELECTOR_FRAGMENT_PATH = "[name='./fragmentPath']";
  var SELECTOR_VARIATION_NAME = "[name='./variationName']";

  // the fragment path field (foundation autocomplete)
  var fragmentPath;

  // keeps track of the current fragment path
  var currentFragmentPath;

  var editDialog;
  var elementsController;

  var ElementsController = function() {
    this._updateFields();
  }

  ElementsController.prototype._updateFields = function() {
    // the variation name field (select)
    this.variationName = editDialog.querySelector(SELECTOR_VARIATION_NAME);
    this.variationNamePath = this.variationName.dataset.fieldPath;
  }

  ElementsController.prototype.disableFields = function() {
    if (this.variationName) {
      this.variationName.setAttribute("disabled", "");
    }
  }

  ElementsController.prototype.prepareRequest = function() {
    var url = Granite.HTTP.externalize(this.variationNamePath) + ".html";

    var data = {
      fragmentPath: fragmentPath.value
    };

    return $.get({
      url: url,
      data: data
    });
  }

  function onFragmentPathChange() {
    var variationNameRequest = this.prepareRequest();

    var self = this;
    $.when(variationNameRequest).then(function(result) {
      var newVariationName = $(result[0]).find(SELECTOR_VARIATION_NAME)[0];

      Coral.commons.ready(newVariationName, function() {
        self.fetchedState = {
          variationName: newVariationName,
          variationNameHTML: result[0]
        };
      });
    });
  }

  function initialize(dialog) {
    // get path of component being edited
    editDialog = dialog;

    // get the fields
    fragmentPath = dialog.querySelector(SELECTOR_FRAGMENT_PATH);

    // initialize variables
    currentFragmentPath = fragmentPath.value;
    elementsController = new ElementsController();

    if (!currentFragmentPath) {
      elementsController.disableFields();
    }

    // register change listener
    $(fragmentPath).off("foundation-field-change").on("foundation-field-change", onFragmentPathChange);
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