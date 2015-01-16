using ResultsView.WebInterface;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace ResultsView.WebInterface
{
    public partial class Charts : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            DataSet ds = DAL.getloadbalancerdata("G");
            divchardata.InnerHtml = "<script>var arrLabels</script>";
            string strLabel = "";
            double count = 0;
            string strData1 = "";
            string strData2 = "";
            string strData3 = "";
            string strData4 = "";
            DateTime dt1= DateTime.Now;
            DateTime dt2;
            //DateTime.Parse((itm.Field<MySql.Data.Types.MySqlDateTime>("dtcreatedon")).ToString())
            for (int i = 0; i < ds.Tables[0].Rows.Count; i++)
            {
                if (i == 0)
                {
                    dt1 = DateTime.Parse((ds.Tables[0].Rows[i]["datetime"].ToString()));

                }
                dt2 = DateTime.Parse((ds.Tables[0].Rows[i]["datetime"].ToString()));
                
                if (i < ds.Tables[0].Rows.Count - 1)
                {
                    strData1 += ds.Tables[0].Rows[i]["requesttotal"] + ",";
                    strData2 += ds.Tables[0].Rows[i]["memory"] + ",";
                    strData3 += ds.Tables[0].Rows[i]["storage"] + ",";
                    strData4 += ds.Tables[0].Rows[i]["computetime"] + ",";
                    strLabel += count.ToString() + ",";
                }
                else
                {
                    strData1 += ds.Tables[0].Rows[i]["requesttotal"];
                    strData2 += ds.Tables[0].Rows[i]["memory"] ;
                    strData3 += ds.Tables[0].Rows[i]["storage"] ;
                    strData4 += ds.Tables[0].Rows[i]["computetime"] ;
                    strLabel += count.ToString();                
                }
                count =dt2.Subtract(dt1).TotalSeconds;
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
            divchardata.InnerHtml = strFullScript;
        }
    }
}