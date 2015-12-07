UIOreEmail = { }

local ui = UIOreEmail
local _svItem = nil

function ui.init()
    _svItem = ccui.Helper:seekNodeByName(ui.Widget, "image_base_email")
    _svItem:retain()

    local btn_closed = ccui.Helper:seekNodeByName(ui.Widget, "btn_closed")
    btn_closed:addTouchEventListener( function(sender, eventType)
        if eventType == ccui.TouchEventType.ended then
            audio.playSound("sound/button.mp3")
            UIManager.popScene()
        end
    end )
end

function ui.getEmailInfo(obj)
    local name, description = "", ""
    local results = obj.int["6"]
    local enemyName = obj.string["3"]
    local content = utils.stringSplit(obj.string["7"], "|")
    local hour, min, sec = 0, 0, 0
    if content[1] then
        local t = tonumber(content[1])
        hour = math.floor(t / 3600)
        min = math.floor(t % 3600 / 60)
        sec = math.floor(t % 60)
    end

    if results == 2 then
        name = "占领资源矿到期"
        description = string.format("您占领的资源矿时间已到，占领时间%02d时%2d分%2d秒，总计获得%s银币，%s进阶石。", hour, min, sec, content[2], content[3])
    elseif results == 3 then
        name = "协助资源矿到期"
        description = string.format("您协助的资源矿占领时间已到，协助时间%02d时%2d分%2d秒，总计获得%d银币，%d进阶石。", hour, min, sec, content[2], content[3])
    elseif results == 4 then
        name = "守护资源矿失败"
        description = string.format("%s夺走了您占领的资源矿，您占领资源矿时间%02d时%2d分%2d秒，总计获得%d银币，%d进阶石。", enemyName, hour, min, sec, content[2], content[3])
    elseif results == 5 then
        name = "协助资源矿失败"
        description = string.format("%s夺走了您协助的资源矿，您协助资源矿时间%02d时%2d分%2d秒，总计获得%d银币，%d进阶石。", enemyName, hour, min, sec, content[2], content[3])
    elseif results == 6 then
        name = "放弃占领资源矿"
        description = string.format("您放弃了占领的资源矿，占领时间%02d时%2d分%2d秒，总计获得%d银币，%d进阶石。", hour, min, sec, content[2], content[3])
    elseif results == 7 then
        name = "放弃协助资源矿"
        description = string.format("您放弃了协助的资源矿，协助时间%02d时%2d分%2d秒，总计获得%d银币，%d进阶石。", hour, min, sec, content[2], content[3])
    elseif results == 8 then
        name = "结束协助资源矿"
        description = string.format("您被矿主%s请离了，您协助资源矿时间%02d时%2d分%2d秒，总计获得%d银币，%d进阶石。", enemyName, hour, min, sec, content[2], content[3])
    elseif results == 9 then
        name = "被动放弃协助资源矿"
        description = string.format("%s放弃了您协助的资源矿，您协助资源矿时间%02d时%2d分%2d秒，总计获得%d银币，%d进阶石。", enemyName, hour, min, sec, content[2], content[3])
    end

    if content[4] then
        local itemProp = utils.getItemProp(content[4])
        description = string.gsub(description, "。", ",")
        description = description .. string.format("并获得了特殊奖励%s×%d，请去领奖中心领取。", itemProp.name, itemProp.count)
    end

    return name, description
end

local function netErrorCallbackFunc(pack)
    local protocol = tonumber(pack.header)
    local msgdata = pack.msgdata
    if protocol == StaticMsgRule.mineFightWin then
        UIOre.showFightResult(ui, -1, msgdata)
    end
end

local function netCallbackFunc(pack)
    local protocol = tonumber(pack.header)
    local msgdata = pack.msgdata
    if protocol == StaticMsgRule.mineBeatBack then
        if msgdata.int.fightType == 0 or msgdata.int.fightType == 1 then
            pvp.loadGameData(pack)
            UIOreInfo.warParam = { msgdata.int.fightType, msgdata.int.playerId, msgdata.int.mineId }
            utils.sendFightData(nil, dp.FightType.FIGHT_MINE, function(isWin)
                if isWin then
                    netSendPackage( {
                        header = StaticMsgRule.mineFightWin,
                        msgdata =
                        {
                            int = { fightType = UIOreInfo.warParam[1], id = UIOreInfo.warParam[2], mineId = UIOreInfo.warParam[3] },
                            string = { coredata = GlobalLastFightCheckData }
                        }
                    } , netCallbackFunc, netErrorCallbackFunc)
                else
                    UIOre.showFightResult(ui, 0)
                end
            end )
            if not UIFightMain.Widget or not UIFightMain.Widget:getParent() then
                UIFightMain.loading()
            else
                UIFightMain.setup()
            end
        end
    elseif protocol == StaticMsgRule.mineFightWin then
        UITeam.checkRecoverState()
        UIOre.showFightResult(ui, 1, msgdata)
    end
end

local function setScrollViewItem(item, obj)
    local text_title = item:getChildByName("text_title")
    local text_time = item:getChildByName("text_time")
    local btn_go = item:getChildByName("btn_go")
    local image_di = item:getChildByName("image_di")
    local image_di_long = item:getChildByName("image_di_long")
    local name, description = ui.getEmailInfo(obj)

    text_title:setString(name)

    local serverTime = utils.GetTimeByDate(obj.string["9"])
    local currentTime = utils.getCurrentTime()
    local subTime = currentTime - serverTime
    local timeText = nil

    if math.floor(subTime /(3600 * 24)) > 0 then
        timeText = math.floor(subTime /(3600 * 24)) .. "天前"
    elseif math.floor(subTime / 3600) > 0 then
        timeText = math.floor(subTime / 3600) .. "小时前"
    elseif math.floor(subTime / 60) > 0 then
        timeText = math.floor(subTime / 60) .. "分钟前"
    elseif math.floor(subTime % 60) > 0 then
        timeText = math.floor(subTime % 60) .. "秒前"
    end
    text_time:setString(timeText)

    if obj.int["6"] == 4 then
        image_di:show():getChildByName("text_info"):setString(description)
        image_di_long:hide()
        btn_go:show():addTouchEventListener( function(sender, eventType)
            if eventType == ccui.TouchEventType.ended then
                audio.playSound("sound/button.mp3")
                if UIOre.mineId and UIOre.mineId ~= 0 then
                    utils.PromptDialog( function()
                        UIManager.showLoading()
                        local packet = { header = UIOre.mineOp == UIOre.MINE_OP_OCCUPY and StaticMsgRule.giveUpOccupy or StaticMsgRule.giveUpAssist, msgdata = { int = { mineId = UIOre.mineId } } }
                        local function giveUpCallback(pack)
                            UIManager.showLoading()
                            netSendPackage( { header = StaticMsgRule.mineBeatBack, msgdata = { int = { minerId = obj.int["5"] } } }, netCallbackFunc)
                        end
                        netSendPackage(packet, giveUpCallback, giveUpCallback)
                    end , string.format("您已经%s一座资源矿，是否放弃%s资源矿并反击？", UIOre.mineOp == UIOre.MINE_OP_OCCUPY and "拥有" or "协助了", UIOre.mineOp == UIOre.MINE_OP_OCCUPY and "" or "协助"))
                else
                    UIManager.showLoading()
                    netSendPackage( { header = StaticMsgRule.mineBeatBack, msgdata = { int = { minerId = obj.int["5"] } } }, netCallbackFunc)
                end
            end
        end )
    else
        btn_go:hide()
        image_di:hide()
        image_di_long:show():getChildByName("text_info"):setString(description)
    end
end

local function compareMail(value1, value2)
    local iTime1 = utils.GetTimeByDate(value1.string["9"])
    local iTime2 = utils.GetTimeByDate(value2.string["9"])
    return iTime1 < iTime2
end

function ui.setup()
    ui.mail = { }
    if net.InstPlayerMail then
        for key, value in pairs(net.InstPlayerMail) do
            if value.int["4"] == 5 then
                table.insert(ui.mail, value)
            end
        end
        utils.quickSort(ui.mail, compareMail)
    end

    local view_success = ccui.Helper:seekNodeByName(ui.Widget, "view_success")
    view_success:removeAllChildren()
    if next(ui.mail) then
        utils.updateScrollView(UIOreEmail, view_success, _svItem, ui.mail, setScrollViewItem)
    end
end

function ui.free()
    local view_success = ccui.Helper:seekNodeByName(ui.Widget, "view_success")
    view_success:removeAllChildren()
    if _svItem and _svItem:getReferenceCount() >= 1 then
        _svItem:release()
        _svItem = nil
    end
end