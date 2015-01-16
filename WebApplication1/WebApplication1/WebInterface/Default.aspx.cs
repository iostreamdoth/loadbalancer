using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace ResultsView.WebInterface
{
    public partial class Default : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            lnkUpload.HRef = "Upload.aspx?id=" + Request.QueryString["id"].ToString();
            lnkBilling.HRef = "ViewBilling.aspx?id=" + Request.QueryString["id"].ToString();
        }
    }
}