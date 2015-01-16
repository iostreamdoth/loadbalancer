<%@ Page Title="" Language="C#" MasterPageFile="~/WebInterface/Master.Master" AutoEventWireup="true" CodeBehind="ZoneChart.aspx.cs" Inherits="ResultsView.WebInterface.ZoneChart" %>
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
        <div class="container">
            <asp:DropDownList runat="server" ID="ddlSelectAlgo">
                <asp:ListItem Text="ZONE 1" Value="zone1"></asp:ListItem>
                <asp:ListItem Text="ZONE 2" Value="zone2"></asp:ListItem>
                <asp:ListItem Text="ZONE 3" Value="zone3"></asp:ListItem>
                <asp:ListItem Text="ZONE 4" Value="zone4"></asp:ListItem>
                <asp:ListItem Text="ZONE 5" Value="zone5"></asp:ListItem>
                <asp:ListItem Text="ZONE 6" Value="zone6"></asp:ListItem>
                <asp:ListItem Text="ZONE 7" Value="zone7"></asp:ListItem>
                <asp:ListItem Text="ZONE 8" Value="zone8"></asp:ListItem>
                <asp:ListItem Text="ZONE 9" Value="zone9"></asp:ListItem>
                
            </asp:DropDownList>
            <asp:DropDownList runat="server" ID="ddlTimeFrame">
                <asp:ListItem Text="3 Seconds" Value=".05"></asp:ListItem>
                <asp:ListItem Text="6 Seconds" Value=".1"></asp:ListItem>
                <asp:ListItem Text="30 Seconds" Value=".5"></asp:ListItem>
                <asp:ListItem Text="1 Minute" Value="1"></asp:ListItem>
                <asp:ListItem Text="5 Minute" Value="5"></asp:ListItem>
                <asp:ListItem Text="1 Hour" Value="60"></asp:ListItem>
                <asp:ListItem Text="10 Hour" Value="600"></asp:ListItem>
            </asp:DropDownList>
            <asp:Button runat="server" ID="btnSelectChart" Text="View Chart" OnClick="btnSelectChart_Click"/>
        </div>
        <div style="width: 60%">
            <div id="divCanvasThing" runat="server">
                <canvas id="canvas" height="450" width="600" style="float: left"></canvas>
            </div>
        </div>
        <div id="divchardata" runat="server">
        </div>
        <div id="divlegend" style="float: left;">
        </div>
    </div>
    <script>
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
