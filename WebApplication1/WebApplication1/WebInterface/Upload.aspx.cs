using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;


namespace ResultsView.WebInterface
{
    public partial class Upload : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            hdnID.Value = Request.QueryString["id"].ToString();
        }

        protected void btnFileUpload_Click(object sender, EventArgs e)
        {
            string MyString="";
            HttpPostedFile flUpload = (HttpPostedFile)(Request.Files[0]);
            //string strHDNId = "0";
            string strHDNId = hdnID.Value;
            Stream MyStream = flUpload.InputStream;
            int FileLen = flUpload.ContentLength;
            byte[] input = new byte[FileLen];
            string[] stronlyFileName=flUpload.FileName.Split('\\');
            string strFileName = stronlyFileName[stronlyFileName.Length-1];
            string strNewFileName = Guid.NewGuid().ToString("N").ToLower().Replace("-", "");
            string strProfKey =strNewFileName ;
            flUpload.SaveAs(Server.MapPath("~/files/") + strNewFileName + ".csv");
            MyStream.Read(input, 0, FileLen);
            
            
            for (int Loop1 = 0; Loop1 < FileLen; Loop1++)
                MyString = MyString + Convert.ToChar(input[Loop1]);
            string[] strArray = Regex.Split(MyString,"\r\n");

            DataSet ds = DAL.getsetfileheader(strFileName, "SJSU", Convert.ToInt32(strHDNId), strProfKey, "I");

            for (int i = 1; i < strArray.Length-1; i++)
            {
                string[] strDataIns = strArray[i].Split(',');
                ds = DAL.getsetstudentdata(0, strProfKey, Convert.ToInt32(strDataIns[0]), strDataIns[1], "I");
            }

        }
    }
}