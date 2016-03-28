/**
 * Created by Kellin on 3/28/16.
 */
$(document).ready(function () {
    var param = self.location.href.split("?");


    var imageSrc = param[1].split('=');

    setImageSrc(imageSrc[1]);
    autoResize(imageSrc[1]);
    clickClose();
});

function setImageSrc(imageSrc){
    $("#previewImage").attr('src', imageSrc);
}

function autoResize(imageSrc) {
    var img = new Image();

    img.onload = function() {
        var NS = (navigator.appName=="Netscape") ? true : false;

        iWidth = (NS)?window.innerWidth:document.body.clientWidth;
        iHeight = (NS)?window.innerHeight:document.body.clientHeight;
        iWidth = this.width - iWidth;
        iHeight = this.height - iHeight;

        if(iHeight > 1000) {
            iHeight = 1000;
        }

        window.resizeBy(iWidth+20, iHeight+70);
        self.focus();

        var extension = imageSrc.split('.');
        $('#formatName').text(extension[extension.length-1]);
    }

    img.src = imageSrc;


};

function clickClose(){
    $("#previewImage").click(function(){
        self.close();
    })
}