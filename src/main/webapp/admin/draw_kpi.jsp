<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="java.util.*" %><%--
  Created by IntelliJ IDEA.
  User: DJCY
  Date: 2024/05/01
  Time: 15:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>绘制折线图</title>
    <!-- 导入绘图库 -->
    <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
    <script src="/js/logout.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }
        #chart-container {
            width: 80%;
            margin: 20px auto;
            background-color: #fff;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        select {
            padding: 10px;
            font-size: 16px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        button {
            padding: 10px 20px;
            font-size: 16px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
    </style>
</head>


<body>
<div id="chart-container">
    <h2>选择 type_id：</h2>
    <select id="type_id_select" name ="type_id_select_pass">
        <%-- 输出所有 type_id 作为选项 --%>
        <%  Map<Integer,List<String>>temp1 = (Map<Integer, List<String>>) request.getSession().getAttribute("datetimeList");
            for (Integer typeId : temp1.keySet()) {
        %>
        <option value="<%= typeId %>"><%= typeId %></option>
        <% } %>
    </select>
    <button onclick="drawChart()">绘图</button>
    <div id="chart" ></div>
</div>



<script>
    // 提取选择的 type_id 的数据并绘制折线图
    function drawChart() {
        //document.getElementById("chartForm").submit();
        var selectedValue = document.getElementById("type_id_select").value;
        var link = "/admin/draw_kpi.jsp?typeId=" + selectedValue;
        window.location.href = link;
    }
    var datetimes= []
    var amounts = []


    <%

        if(request.getParameter("typeId")!=null){
        Integer typeId =Integer.parseInt( request.getParameter("typeId"));
        Map<Integer, List<String>> tmp1 = (Map<Integer, List<String>>)request.getSession().getAttribute("datetimeList");
        Map<Integer, List<Integer>> tmp2 = (Map<Integer, List<Integer>>)request.getSession().getAttribute("amountList");
        List<String> values1 = tmp1.get(typeId);
        List<Integer> values2 = tmp2.get(typeId);

        // 定义原始日期时间格式
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        // 定义目标日期格式
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 创建一个新的列表，用于存储格式化后的日期
        List<String> formattedDates = new ArrayList<>();

        for (String dateTimeString : values1) {
            try {
                // 将原始日期时间字符串解析为Date对象
                Date date = originalFormat.parse(dateTimeString);
                // 格式化Date对象为目标日期格式字符串
                String formattedDate = targetFormat.format(date);

                // 将格式化后的日期添加到新列表中
                formattedDates.add(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 将新的格式化后的日期列表赋值给values1
        values1 = formattedDates;
        System.out.println(values1);

        // 创建一个Map用于存储合并后的数据
        Map<String, Integer> mergedData = new LinkedHashMap<>();

        // 将values1和values2中相同日期对应的值进行合并
        for (int i = 0; i < values1.size(); i++) {
            String date = values1.get(i);
            int value = values2.get(i);

            if (mergedData.containsKey(date)) {
                // 如果日期已经存在于Map中，则将值进行累加
                mergedData.put(date, mergedData.get(date) + value);
            } else {
                // 如果日期不存在于Map中，则直接添加
                mergedData.put(date, value);
            }
        }

        // 更新values1和values2，去除相同的日期
        List<String> uniqueDates = new ArrayList<>(mergedData.keySet());
        List<Integer> mergedValues = new ArrayList<>(mergedData.values());

        values1 = uniqueDates;
        values2 = mergedValues;


        if (values1 != null) {
            for (String value : values1) {
    %>

    datetimes.push("<%= value %>");
    <%
            }
        }

        if (values2 != null) {
            for (Integer value : values2) {
    %>
    amounts.push("<%= value %>");
    <%
            }
        }
    }
    %>

    var plotlyData = [
        {
            x: datetimes,
            y: amounts,
            type: 'bar',
            name: '销售量', // 这是柱状图的数据
        },
        {
            x: datetimes,
            y: amounts,
            type: 'scatter',
            mode: 'lines+markers',
            name: '销售量折线图', // 这是折线图的数据
        }
    ];

    var layout = {
        title: '销售量随时间变化图',
        xaxis: {
            title: '时间'
        },
        yaxis: {
            title: '销售量'
        },
    };

    Plotly.newPlot('chart', plotlyData, layout);
</script>
</body>
</html>

