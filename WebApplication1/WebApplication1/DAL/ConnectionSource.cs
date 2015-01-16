using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ResultsView.DAL
{
    public class ConnectionSource
    {
        public static string getConnectionString(string strType)
        {
            string connStr = "";
            //connStr = String.Format("server={0};user id={1}; password={2};port={3};database=resultview;  Allow Zero Datetime = true ; Convert Zero Datetime=True; pooling=false", "resultview.db.9466358.hostedresource.com", "resultview", "Resultsview@123", "3306");
            connStr = String.Format("server={0};user id={1}; password={2};port={3};database=resultview;  Allow Zero Datetime = true ; Convert Zero Datetime=True; pooling=false", "localhost", "tester", "tester", "3306");
            
            return connStr;
        }
    }
}