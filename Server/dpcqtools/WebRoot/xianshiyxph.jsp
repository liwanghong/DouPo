<%@ page language="java"
	import="java.util.*,com.hygame.dpcq.coon.GameCoon ,com.hygame.dpcq.db.dao.model.ServerAttribute"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<script type="text/javascript">

	//显示限时英雄-排名奖励列表
	function showList() {
		$.getJSON("activities!getLimitHeroRankList.action", {}, function (data){
			if(data == null) {
				alert('获取列表失败');
			}
			 var dataObj=eval("("+data+")");//转换为json对象 
	 		 $("#tbody").empty();
			 $.each(dataObj, function(i,item) { 
				 $("#tbody").append(
						"<tr>"+
       					"<td></td>"+
       					"<td align='center'>"+item.id+"</td>"+
       					"<td align='center'>"+item.startRankNum+"</td>"+
						"<td align='center'>"+item.endRankNum+"</td>"+
						"<td align='center'>"+item.rewards+"</td>"+
           				"<td><a href='#' onclick='update("+item.id+",&quot;"+item.rewards+"&quot;,"+item.startRankNum+","+item.endRankNum+");' ><img src='images/user_edit.png' alt='' title='' border='0' /></a></td>"+
       					"</tr>" 
				); 
	 		}); 
		}); 
	}
	
	//更新限时英雄-排名奖励列表
	function update(id, rewards, startRankNum, endRankNum) {
		var url = 'xianshiyxphUpdate.jsp?id='+id+'&startRankNum='+startRankNum+'&endRankNum='+endRankNum+'&rewards='+rewards;
		$("#right_content").load(url,function(){
			$("#right_content").fadeIn(100);
		});
	}

</script>

<h2>限时英雄排名奖励</h2>

<div style = "text-align:right;">
	<button onclick="showList()">查询当前配置</button>
</div>
<p>
<table id="rounded-corner" style="width:780xp;">
	<tr>
		<th scope="col" class="rounded-company"></th>
		<th scope="col" class="rounded">编号</th>
		<th scope="col" class="rounded">开始排名</th>
		<th scope="col" class="rounded">结束排名</th>
		<th scope="col" class="rounded">奖励物品(可多个)</th>
		<th scope="col" class="rounded-q4">修改</th>
	</tr>
</table>

<div style="overflow-x: auto; overflow-y: auto; height:500px; width:650px;">
	<table id="rounded-corner" style="width:780xp;">
		<tbody id="tbody"></tbody>
	</table>
</div>
