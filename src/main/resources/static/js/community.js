//提交回复
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId,1,content);
}

function comment2target(targetId,type,content) {
    if (!content){
        alert("不能回复内容为空！");
        return;
    }
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:'application/json',
        data:JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        dataType:"json",
        success:function (result) {
            if (result.code==200){
                //刷新页面
                window.location.reload();
            }else {
                if (result.code == 2003){
                    var  isAccepted= confirm(result.message);
                    if (isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=b8bf759495c1fc7dd36f&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(result.message);
                }
            }
        }
    });
}

//二级评论
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId,2,content);
}

//二级菜单展示
function collapseComment(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);
    var toggleClass = comments.toggleClass("in");
    if ( toggleClass.hasClass("in")){
        //判断你是否加载过（children().length）
        if (comments.children().length!=1){
            e.classList.add("active");
        }else {
            $.ajax({
                type:"get",
                url:"/comment/"+id,
                contentType:'application/json',
                data:"",
                dataType:"json",
                success:function (result) {
                    addBody(result,comments);
                    e.classList.add("active");
                }
            });
        }
    }else {
        e.classList.remove("active");
    }
}
function addBody(result,comments) {
    $.each(result.data,function (index,cont) {
        var mediaLeftElement=$("<div/>",{
           "class":"media-left"
        }).append($("<img/>",{
            "class":"media-object",
            "src":cont.user.avatarUrl
        }));

        var mediaBodyElement=$("<div/>",{
            "class":"media-body"
        }).append($("<h5/>",{
            "class":"media-heading",
            "html":cont.user.name
        })).append($("<div/>",{
            "html":cont.comment
        })).append($("<div/>",{
            "class":"menu"
        }).append($("<span/>",{
            "class":"pull-right",
            "html":cont.gmtCreate
        })));
        var mediaHr=("<hr/>",{
            "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comment-sp"
        });

        var mediaElement= $("<div/>",{
            "class":"media"
        }).append(mediaLeftElement)
            .append(mediaBodyElement);
        var commentElement= $("<div/>",{
            "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comment-sp",
        }).append(mediaElement)
            .append(mediaHr);
        comments.prepend(commentElement);
    });

}

//展示标签
function showSelectTag() {
    $("#selectTag").show()
}

//添加标签
function selectTag(e) {

    var value = e.getAttribute("data-tag");
    console.log(value);
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1){
        if (previous){
            $("#tag").val(previous+','+value);
        }else {
            $("#tag").val(value);
        }
    }
}
