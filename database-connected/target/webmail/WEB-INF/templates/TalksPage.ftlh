<#import "common.ftl" as c>

<@c.page>
<div class="form-box">
    <div class="header">New message</div>
    <div class="body">
        <form method="post" action="/talks">
            <input type="hidden" name="action" value="send">
            <div class="field">
                <div class="name">
                    <label for="target_user_login">To</label>
                </div>
                <div class="value">
                    <input id="target_user_login" name="target_user_login" value="${target_user_login!}"/>
                </div>
            </div>
            <div class="field">
                <div class="name">
                    <label for="text">Text</label>
                </div>
                <div class="value">
                    <input id="text" name="text" value="${text!}"/>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Send">
            </div>
            <#if error??>
            <div class="error">
                ${error}
            </div>
            </#if>
        </form>
    </div>
</div>
<div class="datatable">
    <div class="caption">Messages</div>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>From</th>
            <th>Text</th>
        </tr>
        </thead>
        <tbody>
        <#list messages as message>
        <tr>
            <td>
                ${message_index + 1}
            </td>
            <td>
                ${message.sourceUserLogin}
            </td>
            <td>
                ${message.targetUserLogin}
            </td>
            <td>
                ${message.text}
            </td>
        </tr>
        </#list>
    </table>
</div>
</@c.page>
