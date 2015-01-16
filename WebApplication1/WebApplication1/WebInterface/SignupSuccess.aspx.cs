using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace ResultsView.WebInterface
{
    public partial class SignupSuccess : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            string strid = Request.QueryString["id"].ToString();
            idUpload.HRef = "upload.aspx?id=" + strid;
        }
    }
}