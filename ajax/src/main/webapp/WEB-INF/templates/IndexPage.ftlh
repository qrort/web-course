<#-- @ftlvariable name="message" type="java.lang.String" -->

<#import "common.ftl" as c>

<@c.page>
    <#if message??>
        <p>${message}</p>
    </#if>
<div class="articles">
    <div class="label">Articles</div>
    <ul class="body" style="list-style-type:none">
        <li class="no-data">No data</li>
    </ul>
</div>

<template class="article-template">
    <li>
        <article>
            <div class="title">{{title}}</div>
            <div class="information">{{user}}</div>
            <div class="text">{{text}}</div>
            <div class="footer">
                <div class="left">
                    <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                    <span class="positive-score">0</span>
                    <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
                </div>
                <div class="right">
                    <img src="img/date_16x16.png" title="Creation Time" alt="Creation Time"/>
                    {{creationTime}}
                    <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                    <a href="#">68</a>
                </div>
            </div>
        </article>
    </li>
</template>

<script>
    $(function () {
        var $noData = $(".articles .no-data");
        $noData.text("Loading...");
        $.post("", {action: "find"}, function (articles) {
            var $body = $(".articles .body");
            if (articles.length > 0) {
                $body.empty();
                articles.forEach(function (article) {
                    var $li = cloneTemplate($("template.article-template"));
                    $li.find(".title").text(article.title);
                    $li.find(".information").text("By " + article.author.login);
                    $li.find(".text").text(article.text);
                    $li.find(".right").text(article.creationTime);
                    $body.append($li);
                });
            } else {
                $noData.text("No data");
            }
        })
    })
</script>
</@c.page>
