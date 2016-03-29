/**
 * Created by Kellin on 3/29/16.
 */
var webpCheck = [];
webpCheck['basic'] = false;
webpCheck['lossless'] = false;

$(document).ready(function() {
    var hasWebP = (function() {
        var images = {
            basic: "data:image/webp;base64,UklGRjIAAABXRUJQVlA4ICYAAACyAgCdASoCAAEALmk0mk0iIiIiIgBoSygABc6zbAAA/v56QAAAAA==",
            lossless: "data:image/webp;base64,UklGRh4AAABXRUJQVlA4TBEAAAAvAQAAAAfQ//73v/+BiOh/AAA="
        };

        return function(feature) {
            var deferred = $.Deferred();

            $("<img>").on("load", function() {
                // the images should have these dimensions
                if(this.width === 2 && this.height === 1) {
                    deferred.resolve();
                } else {
                    deferred.reject();
                }
            }).on("error", function() {
                deferred.reject();
            }).attr("src", images[feature || "basic"]);

            return deferred.promise();
        }
    })();

    hasWebP().then(function() {
        webpCheck['basic'] = true;
        webpCheckStatusLayer('<b>webp-<font style="color: blue;">Lossy</font></b> 지원가능한 브라우저 입니다.');
    }, function() {
        webpCheck['basic'] = false;
        webpCheckStatusLayer('<b>webp-<font style="color: blue;">Lossy</font></b> 지원불가능한 브라우저 입니다.');
    });

    hasWebP("lossless").then(function() {
        webpCheck['lossless'] = true;
        webpCheckStatusLayer('<b>webp-<font style="color: blue;">Lossless</font></b> 지원가능한 브라우저 입니다.');
    }, function() {
        webpCheck['lossless'] = false;
        webpCheckStatusLayer('<b>webp-<font style="color: blue;">Lossless</font></b> 지원불가능한 브라우저 입니다.');
    });
});