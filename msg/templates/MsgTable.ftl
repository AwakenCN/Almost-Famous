PBMsgId = {
<#list messageInfos as message>
    ${message.name} = ${message.id?c};
</#list>
}

PBMsgTable = {
<#list messageInfos as message>
    [${message.id?c}] = "${message.name}"<#if message_has_next>,</#if>
</#list>
}