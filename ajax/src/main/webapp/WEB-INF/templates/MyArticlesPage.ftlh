<#-- @ftlvariable name="users" type="ru.itmo.webmail.model.domain.User[]" -->
<#import "common.ftl" as c>

<@c.page>
<div class="datatable">
    <div class="caption">My articles</div>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Title</th>
            <th>Hide/Show</th>
        </tr>
        </thead>
        <tbody>
        <#list myArticles as article>
            <tr>
                <td>
                    ${article.id}
                </td>
                <td>
                    ${article.title}
                </td>
                <td>
                    <form method="post" action="/myArticles">
                        <input type="button"
                            <#if article.hidden>
                               value="Show"
                            <#else>
                               value="Hide"
                            </#if>
                        >
                    </form>
                </td>
            </tr>
        </#list>
    </table>
</div>
<script>
    $(function() {
        $("tbody input[type='button']").click(function () {
            var value = $(this).val();
            var notvalue;
            if (value === "Show") {
                notvalue = "Hide";
             } else {
                notvalue = "Show";
            }
            //$(this).val(notvalue);
            let articleId = $(this).closest("tr").find("td")[0].innerHTML;
            $.post("/myArticles", {action: "changePublicity", articleId: articleId, value: value});
            $(this).val(notvalue);
        });
    })
</script>
</@c.page>
