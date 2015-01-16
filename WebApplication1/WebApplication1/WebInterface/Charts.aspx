<%@ Page Title="" Language="C#" MasterPageFile="~/WebInterface/Master.Master" AutoEventWireup="true" CodeBehind="Charts.aspx.cs" Inherits="ResultsView.WebInterface.Charts" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <script src="Include/js/Chart.js"></script>
    <style>
        #fork {
            position: absolute;
            top: 0;
            right: 0;
            border: 0;
        }

        .legend {
            width: 10em;
            border: 1px solid black;
        }

            .legend .title {
                display: block;
                margin: 0.5em;
                border-style: solid;
                border-width: 0 0 0 1em;
                padding: 0 0.3em;
            }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div style="overflow:auto;">
        <div style="width: 60%">
            <div id="divCanvasThing">
                <canvas id="canvas" height="450" width="600" style="float: left"></canvas>
            </div>
        </div>
        <div id="divchardata" runat="server">
        </div>
        <div id="divlegend" style="float: left;">
        </div>
    </div>
    <script>
        ////var randomScalingFactor = function () { return Math.round(Math.random() * 100) };
        //var lineChartData = {
        //    labels: ["January", "February", "March", "April", "May", "June", "July"],
        //    datasets: [
        //		{
        //		    label: "My First dataset", fillColor: "rgba(220,220,220,0.2)", strokeColor: "rgba(220,220,220,1)", pointColor: "rgba(220,220,220,1)", pointStrokeColor: "#fff", pointHighlightFill: "#fff", pointHighlightStroke: "rgba(220,220,220,1)", data: [randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor()]
        //		},
        //		{
        //		    label: "My Second dataset",fillColor: "rgba(151,187,205,0.2)",strokeColor: "rgba(151,187,205,1)",pointColor: "rgba(151,187,205,1)",pointStrokeColor: "#fff",pointHighlightFill: "#fff",pointHighlightStroke: "rgba(151,187,205,1)",data: [randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor()]
        //		}
        //    ]

        //}

        //window.onload = function () {var ctx = document.getElementById("canvas").getContext("2d");window.myLine = new Chart(ctx).Line(lineChartData, {responsive: true});}
        function legend(parent, data) {
            parent.className = 'legend';
            var datas = data.hasOwnProperty('datasets') ? data.datasets : data;

            // remove possible children of the parent
            while (parent.hasChildNodes()) {
                parent.removeChild(parent.lastChild);
            }

            datas.forEach(function (d) {
                var title = document.createElement('span');
                title.className = 'title';
                title.style.borderColor = d.hasOwnProperty('strokeColor') ? d.strokeColor : d.color;
                title.style.borderStyle = 'solid';
                parent.appendChild(title);

                var text = document.createTextNode(d.label);
                title.appendChild(text);
            });
        }

    </script>
</asp:Content>
