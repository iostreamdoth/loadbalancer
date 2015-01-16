using MySql.Data.Types;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace ResultsView.WebInterface
{
    public partial class ZoneChart : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }
        protected void btnSelectChart_Click(object sender, EventArgs e)
        {
            String strValue = ddlSelectAlgo.SelectedValue;
            double timeFrame = Convert.ToDouble(ddlTimeFrame.SelectedValue);

            DateTime startTime = DateTime.MinValue;
            DataSet ds = DAL.getzloadbalancerdata("GZ", "",strValue);
            DataTable dt = ds.Tables[0].Clone();
            int intComputeTime = 0;
            int intMemory = 0;
            int intStorage = 0;
            int intRequestCount = 0;
            foreach (DataRow dr in ds.Tables[0].Rows)
            {


                if (startTime == DateTime.MinValue)
                {
                    startTime = Convert.ToDateTime(dr["datetime"].ToString());
                }

                if ((Convert.ToDateTime(dr["datetime"].ToString()) - startTime).TotalMinutes < timeFrame)
                {
                    intComputeTime += Convert.ToInt32(dr["computetime"].ToString());
                    intMemory += Convert.ToInt32(dr["memory"].ToString());
                    intStorage += Convert.ToInt32(dr["storage"].ToString());
                    intRequestCount += Convert.ToInt32(dr["requesttotal"].ToString());

                }
                else
                {
                    MySqlDateTime datetime = new MySqlDateTime(startTime);


                    DataRow drNew = dt.NewRow();
                    drNew["computetime"] = intComputeTime;
                    drNew["memory"] = intMemory;
                    drNew["storage"] = intStorage;
                    drNew["requesttotal"] = intRequestCount;
                    drNew["datetime"] = datetime;
                    dt.Rows.Add(drNew);
                    intComputeTime = Convert.ToInt32(dr["computetime"].ToString());
                    intMemory = Convert.ToInt32(dr["memory"].ToString());
                    intStorage = Convert.ToInt32(dr["storage"].ToString());
                    intRequestCount = Convert.ToInt32(dr["requesttotal"].ToString());
                    startTime = startTime.AddMinutes(timeFrame);
                }




            }



            //Graph Memory all Algos
            //Graph Storage all Algos
            //Graph Computetime all Algos
            divchardata.InnerHtml = "<script>var arrLabels</script>";
            string strLabel = "";
            double count = 0;
            string strData1 = "";
            string strData2 = "";
            string strData3 = "";
            string strData4 = "";
            DateTime dt1 = DateTime.Now;
            DateTime dt2;
            //DateTime.Parse((itm.Field<MySql.Data.Types.MySqlDateTime>("dtcreatedon")).ToString())
            for (int i = 0; i < dt.Rows.Count; i++)
            {
                if (i == 0)
                {
                    dt1 = DateTime.Parse((dt.Rows[i]["datetime"].ToString()));

                }
                dt2 = DateTime.Parse((dt.Rows[i]["datetime"].ToString()));

                if (i < dt.Rows.Count - 1)
                {
                    strData1 += dt.Rows[i]["requesttotal"] + ",";
                    strData2 += dt.Rows[i]["memory"] + ",";
                    strData3 += dt.Rows[i]["storage"] + ",";
                    strData4 += dt.Rows[i]["computetime"] + ",";
                    strLabel += count.ToString() + ",";
                }
                else
                {
                    strData1 += dt.Rows[i]["requesttotal"];
                    strData2 += dt.Rows[i]["memory"];
                    strData3 += dt.Rows[i]["storage"];
                    strData4 += dt.Rows[i]["computetime"];
                    strLabel += count.ToString();
                }
                count = dt2.Subtract(dt1).TotalSeconds;
            }
            string strcolor1 = "220,220,220";
            string strcolor2 = "189,188,109";
            string strcolor3 = "222,109,98";
            string strcolor4 = "109,167,220";
            string strlegendtemplate = "legendTemplate : \"<ul class=\\\"<%=name.toLowerCase()%>-legend\\\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\\\"background-color:<%=datasets[i].strokeColor%>\\\"></span><%alert(datasets[i].fillColor);%><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>\"";
            string strDataSet1 = "{ label: \"Request\", fillColor: \"rgba(" + strcolor1 + ",0.2)\", strokeColor: \"rgba(" + strcolor1 + ",1)\", pointColor: \"rgba(" + strcolor1 + ",1)\", pointStrokeColor: \"#fff\", pointHighlightFill: \"#fff\", pointHighlightStroke: \"rgba(" + strcolor1 + ",1)\", data: [" + strData1 + "]}";
            string strDataSet2 = "{ label: \"Memory\", fillColor: \"rgba(" + strcolor2 + ",0.2)\", strokeColor: \"rgba(" + strcolor2 + ",1)\", pointColor: \"rgba(" + strcolor2 + ",1)\", pointStrokeColor: \"#fff\", pointHighlightFill: \"#fff\", pointHighlightStroke: \"rgba(" + strcolor2 + ",1)\", data: [" + strData2 + "]}";
            string strDataSet3 = "{ label: \"Storage\", fillColor: \"rgba(" + strcolor3 + ",0.2)\", strokeColor: \"rgba(" + strcolor3 + ",1)\", pointColor: \"rgba(" + strcolor3 + ",1)\", pointStrokeColor: \"#fff\", pointHighlightFill: \"#fff\", pointHighlightStroke: \"rgba(" + strcolor3 + ",1)\", data: [" + strData3 + "]}";
            string strDataSet4 = "{ label: \"Compute time\", fillColor: \"rgba(" + strcolor4 + ",0.2)\", strokeColor: \"rgba(" + strcolor4 + ",1)\", pointColor: \"rgba(" + strcolor4 + ",1)\", pointStrokeColor: \"#fff\", pointHighlightFill: \"#fff\", pointHighlightStroke: \"rgba(" + strcolor4 + ",1)\", data: [" + strData4 + "]}";
            string strlast = ";window.onload = function () {var ctx = document.getElementById(\"canvas\").getContext(\"2d\");window.myLine = new Chart(ctx).Line(lineChartData, {responsive: true," + strlegendtemplate + "}); legend(document.getElementById(\"divlegend\"), lineChartData)}";

            string strFullScript = "<script>var lineChartData = { labels:[" + strLabel + "],datasets: [" + strDataSet1 + "," + strDataSet2 + "," + strDataSet3 + "," + strDataSet4 + "]}" + strlast + "</script>";




            strFullScript = "<script>var lineChartData0 = { labels:[" + strLabel + "],datasets: [" + strDataSet1 + "]}" + "; function fn1 () {document.getElementById(\"h40\").innerText=\"Request Count\";var ctx = document.getElementById(\"canvas0\").getContext(\"2d\");window.myLine = new Chart(ctx).Line(lineChartData0, {responsive: true," + strlegendtemplate + "}); }" + "</script>";
            strFullScript += "<script>var lineChartData1 = { labels:[" + strLabel + "],datasets: [" + strDataSet2 + "]}" + "; function fn2 () {document.getElementById(\"h41\").innerText=\"Memory\";var ctx = document.getElementById(\"canvas1\").getContext(\"2d\");window.myLine = new Chart(ctx).Line(lineChartData1, {responsive: true," + strlegendtemplate + "}); }" + "</script>";
            strFullScript += "<script>var lineChartData2 = { labels:[" + strLabel + "],datasets: [" + strDataSet3 + "]}" + "; function fn3 () {document.getElementById(\"h42\").innerText=\"Storage\";var ctx = document.getElementById(\"canvas2\").getContext(\"2d\");window.myLine = new Chart(ctx).Line(lineChartData2, {responsive: true," + strlegendtemplate + "}); }" + "</script>";
            strFullScript += "<script>var lineChartData3 = { labels:[" + strLabel + "],datasets: [" + strDataSet4 + "]}" + "; function fn4 () {document.getElementById(\"h43\").innerText=\"Compute Time\";var ctx = document.getElementById(\"canvas3\").getContext(\"2d\");window.myLine = new Chart(ctx).Line(lineChartData3, {responsive: true," + strlegendtemplate + "}); }" + "</script>";
            strFullScript += "<script>function fnLoad(){fn1();fn2();fn3();fn4()}window.onload = fnLoad();</script>";



            String strCanvases = "";
            for (int i = 0; i < 4; i++)
            {
                strCanvases += "<canvas id=\"canvas" + i.ToString() + "\" height=\"450\" width=\"600\" style=\"float: left\"></canvas><h4 id=\"h4" + i.ToString() + "\"></h4>";
            }
            divCanvasThing.InnerHtml = strCanvases;
            divchardata.InnerHtml = strFullScript;
            //




        }

    }
}