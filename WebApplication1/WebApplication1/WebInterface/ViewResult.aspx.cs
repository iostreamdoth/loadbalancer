using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace ResultsView.WebInterface
{
    public partial class ViewResult : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void btnViewResult_Click(object sender, EventArgs e)
        {
            string strStudentID = Request.Form["username"];
            string strPublicKey = Request.Form["password"];
            DataSet ds = DAL.getsetstudentdata(0, strPublicKey, Convert.ToInt32(strStudentID), "", "GI");
            DataSet ds2 = DAL.getsetfileheader("", "SJSU", 0, strPublicKey, "GI");
            if (ds != null)
            {
                if (ds.Tables[0].Rows.Count != 0)
                {

                    string strContext = "<table><tr><td>Subject</td><td>" + ds2.Tables[0].Rows[0]["filekey"].ToString() + "</td><tr><td>Student ID</td><td>" + ds.Tables[0].Rows[0]["studentid"] + "</td></tr><tr><td>Grades</td><td>" + ds.Tables[0].Rows[0]["grade"] + "</td></tr></table>";
                    viewResultsRow.InnerHtml = strContext;
                }
                else
                {
                    Response.Redirect("Error.aspx");
                }
            }
            else
            {
                Response.Redirect("Error.aspx");
            }
        }
    }
}