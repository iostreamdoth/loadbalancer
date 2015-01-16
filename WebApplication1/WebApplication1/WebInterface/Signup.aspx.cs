using ResultsView.DAL;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using ResultsView.DAL;

namespace ResultsView.WebInterface
{
    public partial class Signup : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void btnSignUp_Click(object sender, EventArgs e)
        {
            string strUserID;
            string strPassword;
            string strConfirmPassword;
            strUserID = Request.Form["signupusername"].ToString();
            strPassword = Request.Form["signuppassword"].ToString();
            strConfirmPassword = Request.Form["signupconfpassword"].ToString();

            DataSet ds = DAL.getsetprofessor(strUserID, strPassword, 0, "G");

            if (ds.Tables[0].Rows.Count == 0)
            {
                ds = DAL.getsetprofessor(strUserID, strPassword, 0, "I");
                string strid = ds.Tables[0].Rows[0]["idlogin"].ToString();
                Response.Redirect("SignupSuccess.aspx?id=" + strid);

            }

            else
            {
                Response.Redirect("Error.aspx");
                //Show Error
            }

        }

        protected void btnSignIn_Click(object sender, EventArgs e)
        {
            string strUserID;
            string strPassword;
            strUserID = Request.Form["signinusername"].ToString();
            strPassword = Request.Form["signinpassword"].ToString();
            DataSet ds = DAL.getsetprofessor(strUserID, strPassword, 0, "G");

            if (ds.Tables[0].Rows.Count == 1)
            {
                string strDBPassword = ds.Tables[0].Rows[0]["password"].ToString().Trim();
                if (strDBPassword == strPassword)
                {
                    Session["id"] = ds.Tables[0].Rows[0]["idlogin"].ToString().Trim();
                    Response.Redirect("Default.aspx?id=" + ds.Tables[0].Rows[0]["idlogin"].ToString());
                }
                else
                {
                    Response.Redirect("Error.aspx");
                }
            }
            else
            {
                Response.Redirect("Error.aspx");
                //Show Error
            }
        }
    }
}