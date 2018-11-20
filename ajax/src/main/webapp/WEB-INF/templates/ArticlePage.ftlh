<#import "common.ftl" as c>

<@c.page>
<div class="article form-box">
    <div class="header">Create article</div>
    <div class="body">
        <form method="post" action="/article">
            <input type="hidden" name="action" value="create">
            <div class="field">
                <div class="name">
                    <label for="title">Title</label>
                </div>
                <div class="value">
                    <input id="title" name="title" value="${title!}"/>
                </div>
            </div>
            <div class="field">
                <div class="name">
                    <label for="text">Text</label>
                </div>
                <div class="value">
                    <textarea id="text" name="text">${text!}</textarea>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Create">
            </div>
            <div class="error"></div>
        </form>
    </div>
</div>
<script>
    $(function() {
        $(".article form").submit(function () {
            var title = $(this).find("input[name='title']").val();
            var text = $(this).find("textarea[name='text']").val();
            var $error = $(this).find(".error").text("");
            $.post("", {action: "create", title: title, text: text}, function (response) {
                if (response["success"]) {
                    setMessage("Article created.");
                    document.location.href = "/";
                } else {
                    $error.text(response["error"]);
                }
            });
            return false;
        });
    })
</script>
</@c.page>
