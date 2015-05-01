<html xml:lang="en" lang="en">

<head>
	<title>SignMeUp Analytics </title>
	<link rel="stylesheet" href="/css/analytics.css" type="text/css" media="screen" />
</head>

<body>

<div id="tab_box" class="box">
	<h4> Analytics <small>Select a Tab</small></h4>

	<div class="tabbed_area">
		<ul class="tabs">
		<li><a href="javascript:tabSwitch('tab_1', 'content_1');" id="tab_1" class="active">Day of Week View</a></li>
		<li><a href="javascript:tabSwitch('tab_2', 'content_2');" id="tab_2">Pie Chart View</a></li>
		<li><a href="javascript:tabSwitch('tab_3', 'content_3');" id="tab_3">Student Histogram</a></li>
		</ul>

		<div id="content_1" class="content">
    		<!--Div that will hold the bar chart-->
    		<div id="bar_chart_div"></div>
		</div>

		<div id="content_2" class="content">
			<div id="pie_chart_div"></div>
		</div>

		<div id="content_3" class="content">
			<div id="histogram_div"></div>
		</div>

	</div>

</div>

<div id="filter_box" class="box">
	<div>
	<h4> Filter By Content: </h4>
	</div>

	<div id="select_0" class="filter_selection_button">
	Filter by course:
	<select>
	</select>	
	</div>

	<div id="select_1" class="filter_selection_button">
	Filter by category:
	<select>
	</select>	
	</div>

	<div id="select_2" class="filter_selection_button">
	Filter by assignment:
	<select>
	</select>
	</div>	

	<div id="select_3" class="filter_selection_button">
	Filter by topic:
	<select>
	</select>	
	</div>
</div>

<script src="/js/jquery-2.1.1.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script src="/js/analytics.js" type="text/javascript"></script>

</body>
</html>