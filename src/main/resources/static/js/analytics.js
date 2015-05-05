var weekData = [];
var studentFrequencyData = [];


function setupFilterSelectButtons() {

  var NUM_SELECT_BUTTONS = 4;
  var selectButtons = [];
  var selectOptions = [[]];

  //updates the global data and also
  //the selectOptions
  function filterAndUpdate(num, filterParameters) {

    $.post("/analytics_query", {"filterParameters": JSON.stringify(filterParameters)}, function(responseJSON) {

    var responseObject = JSON.parse(responseJSON);
    console.log(responseObject);

      if (num + 1 < NUM_SELECT_BUTTONS) {
        selectOptions[num + 1] = responseObject.selectOptions;
        setOptionsTo(selectButtons[num + 1], selectOptions[num + 1]);
      }

      weekData = responseObject.weekData;
      for (var i = 0; i < weekData.length; i++) {
        weekData[i][1] = Number(weekData[i][1]);
      }

      studentFrequencyData = responseObject.studentFrequency;
      for (var i = 0; i < studentFrequencyData.length; i++) {
        studentFrequencyData[i][0] = String(studentFrequencyData[i][0]);
      }
      studentFrequencyData.splice(0, 0, ["student id", "Visits"]);

      drawHistogram();
      drawBarChart();
      drawPieChart();
      console.log(studentFrequencyData);

    });
  }

  function setOptionsTo(button, optionList) {
    var nextHTML = "<option value=\"-----\">-----</option>";
    if (optionList.length > 0) {
      for (var i = 0; i < optionList.length; i++) {
        nextHTML += "<option value=" + optionList[i][1] + ">" + optionList[i][0] + "</option>";
      }
      $("select", button[0])[0].innerHTML = nextHTML;
      button[0].style.visibility = 'visible';
    }
  }

  function selectFunction(button) {

    //hide all the next buttons:
    for (var i = button.num + 1; i < NUM_SELECT_BUTTONS; i++) {
      selectButtons[i][0].style.visibility = 'hidden';
    }

    //store up all the values of the select menus
    var filterParameters = [];
    for (var i = 0; i < button.num; i++) {
      var s = $("select", selectButtons[i]).val();
      filterParameters[i] = s;
    } 
    s = $("select", selectButtons[i]).val();
    if (s == "-----") {
      filterAndUpdate(button.num - 1, filterParameters);
    } else {
      filterParameters[button.num] = s;
      filterAndUpdate(button.num, filterParameters);
    }
  }

  for (var i = 0; i < NUM_SELECT_BUTTONS; i++) {
    selectButtons[i] = $("#select_" + String(i));
    selectButtons[i].num = i;
    $("select", selectButtons[i])[0].oninput =
      function(){
        var currentButton = selectButtons[i];
        return function(){selectFunction(currentButton);}
      }();
  }

  filterAndUpdate(-1, []);
  setOptionsTo(selectButtons[0], selectOptions[0]);

}

setupFilterSelectButtons();


/*
Handles switching the tabs between the three display modes.
*/
function tabSwitch(new_tab, new_content) {
     
    $("#content_1")[0].style.display = 'none';
    $("#content_2")[0].style.display = 'none';
    $("#content_3")[0].style.display = 'none';        
    document.getElementById(new_content).style.display = 'block';   
     
 
    $("#tab_1")[0].className = '';
    $("#tab_2")[0].className = '';
    $("#tab_3")[0].className = '';        
    document.getElementById(new_tab).className = 'active';
}



// Load the Visualization API and the piechart package.
google.load('visualization', '1.0', {'packages':['corechart']});

// Set a callback to run when the Google Visualization API is loaded.
google.setOnLoadCallback(drawBarChart);
google.setOnLoadCallback(drawPieChart);
google.setOnLoadCallback(drawHistogram);

// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.
function drawBarChart() {

    // Create the data table.
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Day of the Week');
    data.addColumn('number', 'Visits');
    data.addRows(weekData);
    // Set chart options
    var options = {'title':'Hours Frequency',
                   'width':400,
                   'height':300};

    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.BarChart(document.getElementById('bar_chart_div'));
    chart.draw(data, options);
}

//IN PROGRESS
function drawHistogram() {
  console.log("drawing histogram");
  // Create the data table.
  var data = google.visualization.arrayToDataTable(studentFrequencyData);


  // Set chart options
   var options = {'title':'Hours Frequency',
                 'width':400,
                 'height':300,
                  'legend':{position: 'top', textStyle: {color: 'blue', fontSize: 10}}};

  // Instantiate and draw our chart, passing in some options.
  console.log("about to draw");
  var chart = new google.visualization.Histogram(document.getElementById('histogram_div'));
  chart.draw(data, options); 
}

// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.
function drawPieChart() {

    // Create the data table.
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Day of the Week');
    data.addColumn('number', 'Visits');
    data.addRows(weekData);
    // Set chart options
    var options = {'title':'Hours Frequency',
                   'width':400,
                   'height':300,
                    'is3d':true};

    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.PieChart(document.getElementById('pie_chart_div'));
    chart.draw(data, options);
}