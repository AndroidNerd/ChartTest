# ChartTest
一个图表练习<br>

![image](https://github.com/AndroidNerd/ChartTest/blob/master/pic/chart.gif?raw=true)<br>
核心代码：<br>
 <pre><code> chart.setPoints(points);
  String str[] = new String[]{"20", "15", "10", "5", "0"};
  chart.setXst(str);
  String str2[] = new String[]{"20", "21", "22", "23", "24", "25", "26", "27"};//个数应和point点数相同
  chart.setYst(str2);
  chart.setType(CurveChart.TYPE.FOLD);//默认曲线，此为折线
  </code></pre>
<br>![image](https://github.com/AndroidNerd/ChartTest/blob/master/pic/chart.jpg?raw=true)
