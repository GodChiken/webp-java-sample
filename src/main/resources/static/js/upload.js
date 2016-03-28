/**
 * Created by Kellin on 3/28/16.
 */
function numberWithCommas (x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function preview (obj) {
    window.open('/html/preview.html?url='+$(obj).attr('src'), '', 'width=300, height=400');
}

$(document).on('ready', function() {
    $("#input-24").fileinput({
        initialPreview: [],
        overwriteInitial: false,
        maxFileSize: 10000,
        initialCaption: "테스트할 이미지 파일을 업로드 해주세요. 최대 용량은 10mb 이며 jpg,png,bmp 파일만 올려주세요."
    });

    var formObj = $('#webpTestForm');
    formObj.attr('method', 'post');
    formObj.attr('action', '/doUpload');

    var statusArea = $('#uploadStatusLayer');

    formObj.ajaxForm({
        beforeSubmit: function (data, form, option, xhr) {
            statusArea.empty();

            var fileCount = 0;

            $('.file-preview-frame').each(function () {
                fileCount = $(this).attr('data-fileindex')
            });

            fileCount = parseInt(fileCount) + 1;

            for (var i=0; i<fileCount; i++) {
                var html = '<div id="imageInfo_'+i+'" class="imageInfoLayer"><img src="/image/ajax-loader.gif" class="ajaxLoadingImage" /></div>';
                statusArea.html(statusArea.html() + html);
            }

            statusArea.show();
        },
        success: function (response, status) {
            statusArea.empty();

            if (response.status == 200) {
                for (var key in response.resultData) {
                    var imageDataTmp = response.resultData[key];

                    for (var i=0; i<imageDataTmp.length; i++) {
                        var format = imageDataTmp[i].fileName.split('_');

                        if (format.length > 1) {
                            format = format[1].split('.');
                            format = format[0] + ' ' + '<font style="color: darkblue;">' + format[1] + '</font>';
                        } else {
                            format = format[0].split('.');
                            format = format[1];
                        }

                        var html = '<div id="imageInfo_'+i+'" class="imageInfoLayer">'
                            + '<div class="imagePreview"><img src="'+imageDataTmp[i].fileUrl+'" width=320 height= 240 onClick="preview(this)" /></div>'
                            + ' <ul>'
                            + '     <li>포멧 : <b>'+format+'</b></li>'
                            + '     <li>이미지명 : '+imageDataTmp[i].fileName+'</li>'
                            + '     <li>용량 : <font style="color: crimson"><b>'+numberWithCommas(imageDataTmp[i].fileSize)+'</b> KB</font></li>'
                            + ' </ul>'
                            + '</div>';

                        statusArea.html(statusArea.html() + html);
                    }
                }
            } else {
                statusArea.hide();
                alert('업로드 서버에 문제가 발생했습니다. 잠시후 다시 시도해 주세요.');
            }
        },
        timeout: 30000,
        error: function () {
        }
    });
});