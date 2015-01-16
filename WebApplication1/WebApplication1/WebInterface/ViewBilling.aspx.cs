using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace ResultsView.WebInterface
{
    public partial class ViewBilling : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (Request.QueryString["id"] != null)
            {
                hdnID.Value = Request.QueryString["id"].ToString();
            }
            else
            {
                hdnID.Value = "1";
            }
            DataSet ds = DAL.getsetbillengine(0, 0, 0, Convert.ToInt32(hdnID.Value), "", "GI", "");


            Dictionary<string, string> osList = new Dictionary<string, string>
        {
            {"Windows NT 6.3", "Windows 8.1"},
            {"Windows NT 6.2", "Windows 8"},
            {"Windows NT 6.1", "Windows 7"},
            {"Windows NT 6.0", "Windows Vista"},
            {"Windows NT 5.2", "Windows Server 2003"},
            {"Windows NT 5.1", "Windows XP"},
            {"Windows NT 5.0", "Windows 2000"},
            {"Open BSD" , "OpenBSD"},
            {"Sun OS" , "SunOS"},
            {"Linux" , "Linux/X11"},
            {"Mac OS" , "(Macintosh)"},
            {"QNX" , "QNX"},
            {"BeOS" , "BeOS"},
            {"OS/2" , "OS/2"},
            {"iOS","iOS"},
            {"Android","Android"},
            {"Windows Phone","Windows Phone"},
            {"Windows Phone 8","Windows Phone 8"},
            {"Windows Phone 8.1","Windows Phone 8.1"},
            {"iPhone","iOS"},
            {"Bada","Bada"}

        };
            Dictionary<string, int[]> dictAll = new Dictionary<string, int[]>();
            Dictionary<string, int[]> dictOs = new Dictionary<string, int[]>();
            Dictionary<string, int[]> dictMobie = new Dictionary<string, int[]>();

            string strData1 = "";
            string strData2 = "";
            string strData3 = "";
            string strData4 = "";
            int c = 0;
            string strLabel = "";

            List<vData> lstAllData;
            int count;
            string addkey;
            string oskey;
            DateTime sample;
            int intComputeTime = 0;
            int intMemory = 0;
            int intStorage = 0;
            int intRequest = 0;

            for (int i = 0; i < ds.Tables[0].Rows.Count; i++)
            {
                oskey = ds.Tables[0].Rows[i]["clienttype"].ToString().Trim() ;
                addkey = ds.Tables[0].Rows[i]["datetime"].ToString();
                sample = Convert.ToDateTime(addkey);
                addkey = sample.Month.ToString() + "-" + sample.Day.ToString() + "-" + sample.Year.ToString() + "-" + sample.Hour.ToString() + "-" + sample.Minute.ToString();
                count = Convert.ToInt32(ds.Tables[0].Rows[i]["count"]);
                if (!dictAll.ContainsKey(addkey))
                {
                    dictAll.Add(addkey, new int[] { Convert.ToInt32(ds.Tables[0].Rows[i]["computetime"]) * count, Convert.ToInt32(ds.Tables[0].Rows[i]["storage"]) * count, Convert.ToInt32(ds.Tables[0].Rows[i]["memory"]) * count, count });
                }
                else
                {
                    string key = addkey;
                    dictAll[key] = new int[] { dictAll[key][0] + (Convert.ToInt32(ds.Tables[0].Rows[i]["computetime"]) * count), dictAll[key][1] + (Convert.ToInt32(ds.Tables[0].Rows[i]["storage"]) * count), dictAll[key][2] + (Convert.ToInt32(ds.Tables[0].Rows[i]["memory"]) * count), dictAll[key][3] + (count) };
                }
                // OS data COnsumption
                if (!dictOs.ContainsKey(oskey))
                {
                    dictOs.Add(oskey, new int[] { Convert.ToInt32(ds.Tables[0].Rows[i]["computetime"]) * count, Convert.ToInt32(ds.Tables[0].Rows[i]["storage"]) * count, Convert.ToInt32(ds.Tables[0].Rows[i]["memory"]) * count, count });
                }
                else
                {
                    string key = oskey;
                    dictOs[key] = new int[] { dictOs[key][0] + (Convert.ToInt32(ds.Tables[0].Rows[i]["computetime"]) * count), dictOs[key][1] + (Convert.ToInt32(ds.Tables[0].Rows[i]["storage"]) * count), dictOs[key][2] + (Convert.ToInt32(ds.Tables[0].Rows[i]["memory"]) * count), dictOs[key][3] + (count) };
                }




            }

            foreach (KeyValuePair<string, int[]> kp in dictAll)
            {
                if (c == 0)
                {
                    intComputeTime += kp.Value[0];
                    intMemory += kp.Value[1];
                    intStorage += kp.Value[2];
                    intRequest += kp.Value[3];


                    strData1 += kp.Value[3].ToString();
                    strData2 += kp.Value[2].ToString();
                    strData3 += kp.Value[1].ToString();
                    strData4 += kp.Value[0].ToString();
                    strLabel += kp.Key;
                }
                else
                {
                    intComputeTime += kp.Value[0];
                    intStorage += kp.Value[1];
                    intMemory += kp.Value[2];
                    intRequest += kp.Value[3];

                    strData1 += "," + kp.Value[3].ToString();
                    strData2 += "," + kp.Value[2].ToString();
                    strData3 += "," + ((intStorage) / 2).ToString();
                    strData4 += "," + kp.Value[0].ToString();
                    strLabel += "," + kp.Key;
                }
                c++;
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
            string strlast = ";function fn1() {var ctx = document.getElementById(\"canvas\").getContext(\"2d\");window.myLine = new Chart(ctx).Line(lineChartData, {responsive: true," + strlegendtemplate + "}); legend(document.getElementById(\"divlegend\"), lineChartData)}";

            string strFullScript = "<script>var lineChartData = { labels:[" + strLabel + "],datasets: [" + strDataSet1 + "," + strDataSet2 + "," + strDataSet3 + "," + strDataSet4 + "]}" + strlast + "</script>";
            divchardata.InnerHtml = strFullScript;
            double dblComputeCharges = (intComputeTime / 1000.0) * .01;
            double dblMemory = (intMemory / 1000.0) * .021;
            double dblStorage = (intStorage / 1000.0) * .03;
            string strBillingData = "<script>function  fn2(){var ctx1 = document.getElementById(\"canvas1\").getContext(\"2d\"); var data = [{value: " + dblComputeCharges.ToString() + ",color:\"#F7464A\",highlight: \"#FF5A5E\",label: \"Compute Charge\"},{value: " + dblMemory.ToString() + ",color:\"#46BFBD\",highlight: \"#5AD3D1\",label: \"Memory Charge\"},{value: " + dblStorage.ToString() + ",color:\"#FDB45C\",highlight: \"#FFC870\",label: \"Storage\"}];var myPieChart = new Chart(ctx1).Pie(data,options);}; </script>";


            //
            string strTable = "<br/><table class=\"table table-striped\">";
            strTable += "<tr><td>Entity</td><td>Consumption</td><td>Cost</td></tr>";
            strTable += "<tr><td>Compute Time</td><td>" + intComputeTime.ToString() + "</td><td>$" + dblComputeCharges + "</td></tr>";
            strTable += "<tr><td>Memory</td><td>" + intMemory.ToString() + "</td><td>$" + dblMemory + "</td></tr>";
            strTable += "<tr><td>Storage</td><td>" + intStorage.ToString() + "</td><td>$" + dblStorage + "</td></tr>";
            strTable += "<tr><td>Requests Count</td><td>" + intRequest.ToString() + "</td><td>$" + 0.0 + "</td></tr>";
            strTable += "<tr><td>Total Bill</td><td></td><td>$" + (dblComputeCharges + dblMemory + dblStorage).ToString() + "</td></tr>";
            strTable += "</table>";
            totaltable.InnerHtml = strTable + strBillingData;


            c = 0;
            strData1 = "";
            strData2 = "";
            strData3 = "";
            strData4 = "";
            strLabel = "";


            foreach (KeyValuePair<string, int[]> kp in dictOs)
            {
                if (c == 0)
                {
                    strData1 += kp.Value[3].ToString();
                    strData2 += kp.Value[2].ToString();
                    strData3 += kp.Value[1].ToString();
                    strData4 += kp.Value[0].ToString();
                    strLabel += "\""+kp.Key+"\"";
                }
                else
                {
                    intComputeTime += kp.Value[0];
                    intStorage += kp.Value[1];
                    intMemory += kp.Value[2];
                    intRequest += kp.Value[3];

                    strData1 += "," + kp.Value[3].ToString();
                    strData2 += "," + kp.Value[2].ToString();
                    strData3 += "," + ((intStorage) / 2).ToString();
                    strData4 += "," + kp.Value[0].ToString();
                    strLabel += "," + "\"" + kp.Key + "\"";
                }
                c++;
            }

            strDataSet1 = "{ label: \"Request\", fillColor: \"rgba(" + strcolor1 + ",0.2)\", strokeColor: \"rgba(" + strcolor1 + ",1)\", pointColor: \"rgba(" + strcolor1 + ",1)\", pointStrokeColor: \"#fff\", pointHighlightFill: \"#fff\", pointHighlightStroke: \"rgba(" + strcolor1 + ",1)\", data: [" + strData1 + "]}";
            strDataSet2 = "{ label: \"Memory\", fillColor: \"rgba(" + strcolor2 + ",0.2)\", strokeColor: \"rgba(" + strcolor2 + ",1)\", pointColor: \"rgba(" + strcolor2 + ",1)\", pointStrokeColor: \"#fff\", pointHighlightFill: \"#fff\", pointHighlightStroke: \"rgba(" + strcolor2 + ",1)\", data: [" + strData2 + "]}";
            strDataSet3 = "{ label: \"Storage\", fillColor: \"rgba(" + strcolor3 + ",0.2)\", strokeColor: \"rgba(" + strcolor3 + ",1)\", pointColor: \"rgba(" + strcolor3 + ",1)\", pointStrokeColor: \"#fff\", pointHighlightFill: \"#fff\", pointHighlightStroke: \"rgba(" + strcolor3 + ",1)\", data: [" + strData3 + "]}";
            strDataSet4 = "{ label: \"Compute time\", fillColor: \"rgba(" + strcolor4 + ",0.2)\", strokeColor: \"rgba(" + strcolor4 + ",1)\", pointColor: \"rgba(" + strcolor4 + ",1)\", pointStrokeColor: \"#fff\", pointHighlightFill: \"#fff\", pointHighlightStroke: \"rgba(" + strcolor4 + ",1)\", data: [" + strData4 + "]}";


            string sreReq = "<script></script>";
            string sreMem = "<script></script>";
            string sreStg = "<script></script>";
            string sreCmp = "<script></script>";
            strBillingData = "<script>function fn3(){var radarReq = { labels:[" + strLabel + "],datasets: [" + strDataSet1 + "]};var ctxreq = document.getElementById(\"canvasreq\").getContext(\"2d\"); var datareq  = radarReq ;var myPieChart = new Chart(ctxreq).Radar(datareq,options);}; </script>";
            strBillingData += "<script>function fn4(){var radarmem = { labels:[" + strLabel + "],datasets: [" + strDataSet3 + "]};var ctxmem = document.getElementById(\"canvasmem\").getContext(\"2d\"); var datamem = radarmem ;var myPieChart = new Chart(ctxmem).Radar(datamem,options);}; </script>";
            strBillingData += "<script>function fn5(){var radarSt = { labels:[" + strLabel + "],datasets: [" + strDataSet2 + "]};var ctxst = document.getElementById(\"canvasst\").getContext(\"2d\"); var datast =radarSt ;var myPieChart = new Chart(ctxst).Radar(datast,options);}; </script>";
            strBillingData += "<script>function fn6(){var radarcomp = { labels:[" + strLabel + "],datasets: [" + strDataSet4 + "]};var ctxcomp = document.getElementById(\"canvascomp\").getContext(\"2d\"); var datacomp= radarcomp ;var myPieChart = new Chart(ctxcomp).Radar(datacomp,options);}; ";
            strBillingData += ";window.onload = function(){fn1();fn2();fn3();fn4();fn5();fn6();};</script>";
            totaltable.InnerHtml += strBillingData;
        }

    }
    public class vData
    {
        string datetime;
        int[] variable;
    }
}