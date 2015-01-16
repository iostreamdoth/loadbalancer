using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Threading;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace ResultsView.WebInterface
{
    public partial class Master : System.Web.UI.MasterPage
    {
       
        

        void DoThreadWork(){
            for (int i = 0; i < 100; i++)
            {
                for (int j = 0; j < 1000; j++)
                {
                    double d = 10.0;
                    double n = 3;
                    double k = d / n;
                }
            }
        }


        protected void Page_Load(object sender, EventArgs e)
        {
            if (Session["id"] == null)
            {
                lnkSignIn.Visible = true;
                lnkSignOut.Visible = false;
            }
            else
            {
                lnkSignIn.Visible = false;
                lnkSignOut.Visible = true;
            }
            hdnLatitude1.Value = Request.Form["hdnLatitude"];
            hdnLongitude1.Value = Request.Form["hdnLongitude"] ;
            //Thread.Sleep(1000);
            string strclienttype = Request.UserAgent;


            Thread thread = new Thread(DoThreadWork);
            thread.Start();

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
            string userAgentText = HttpContext.Current.Request.UserAgent;

            if (userAgentText != null)
            {
                int startPoint = userAgentText.IndexOf('(') + 1;
                int endPoint = userAgentText.IndexOf(';');

                try
                {
                    string osVersion = userAgentText.Substring(startPoint, (endPoint - startPoint));
                    startPoint = endPoint + 1;
                    string osSubVersion = userAgentText.Substring(startPoint, userAgentText.Length - (startPoint));

                    try
                    {
                        strclienttype = osList[osVersion];
                        try
                        {
                            osSubVersion = userAgentText.Substring(startPoint, osSubVersion.IndexOf(';'));
                            strclienttype = osSubVersion;
                        }
                        catch
                        {
                        }
                    }
                    catch (Exception ex)
                    {
                        strclienttype = "Unknown";
                    }
                }
                catch (Exception ex)
                {
                    strclienttype = "Unknown";
                }
            }
            if (Request.QueryString["id"] != null)
            {
                string strId = Request.QueryString["id"].ToString();
                string strRequestType = Request.FilePath.ToString();





                int intComputeTime = 2;
                int intStorage = 2;
                int intMemory = 2;
                DataSet ds = DAL.getsetbillengine(intMemory, intStorage, intComputeTime, Convert.ToInt32(strId), strRequestType, "GR", strclienttype);
                if (ds.Tables[0].Rows.Count == 1)
                {
                    ds = DAL.getsetbillengine(intMemory, intStorage, intComputeTime, Convert.ToInt32(strId), strRequestType, "U", strclienttype);
                }
                else
                {
                    ds = DAL.getsetbillengine(intMemory, intStorage, intComputeTime, Convert.ToInt32(strId), strRequestType, "I", strclienttype);
                }
            }

            else if (Request.FilePath.ToString().ToUpper().Contains("VIEWRESULT.ASPX"))
            {
                string strStudentID = Request.Form["username"];
                string strPublicKey = Request.Form["password"];
                DataSet ds = DAL.getsetstudentdata(0, strPublicKey, Convert.ToInt32(strStudentID), "", "GI");
                DataSet ds2 = DAL.getsetfileheader("", "SJSU", 0, strPublicKey, "GI");

                string strId = ds2.Tables[0].Rows[0]["idlogin"].ToString();



                string strRequestType = Request.FilePath.ToString();
                int intComputeTime = 2;
                int intStorage = 0;
                int intMemory = 2;
                ds = DAL.getsetbillengine(intMemory, intStorage, intComputeTime, Convert.ToInt32(strId), strRequestType, "GR", strclienttype);
                if (ds.Tables[0].Rows.Count == 1)
                {
                    ds = DAL.getsetbillengine(intMemory, intStorage, intComputeTime, Convert.ToInt32(strId), strRequestType, "U", strclienttype);
                }
                else
                {
                    ds = DAL.getsetbillengine(intMemory, intStorage, intComputeTime, Convert.ToInt32(strId), strRequestType, "I", strclienttype);
                }
            }

            // Insert Data into DB



        }

        protected void lnkHome_Click(object sender, EventArgs e)
        {
            if (Session["id"] != null)
            {
                Response.Redirect("Default.aspx?id=" + Session["id"].ToString());
            }
            else
            {
                Response.Redirect("Signup.aspx");
            }

        }

        protected void lnkUpldF_Click(object sender, EventArgs e)
        {
            if (Session["id"] != null)
            {
                Response.Redirect("Upload.aspx?id=" + Session["id"].ToString());
            }
            else
            {
                Response.Redirect("Signup.aspx");
            }
        }

        protected void lnkBillingU_Click(object sender, EventArgs e)
        {
            if (Session["id"] != null)
            {
                Response.Redirect("ViewBilling.aspx?id=" + Session["id"].ToString());
            }
            else
            {
                Response.Redirect("Signup.aspx");
            }
        }

        protected void lnkSignIn_Click(object sender, EventArgs e)
        {
            if (Session["id"] != null)
            {
                Response.Redirect("Signup.aspx?id=" + Session["id"].ToString());
            }
            else
            {
                Response.Redirect("Signup.aspx");
            }
        }

        protected void lnkHome1_Click(object sender, EventArgs e)
        {
            if (Session["id"] != null)
            {
                Response.Redirect("Index.aspx?id=" + Session["id"].ToString());
            }
            else
            {
                Response.Redirect("Index.aspx");
            }
        }

        protected void lnkSignOut_Click(object sender, EventArgs e)
        {
            Session["id"] = null;
            Response.Redirect("Index.aspx");
        }
    }
}