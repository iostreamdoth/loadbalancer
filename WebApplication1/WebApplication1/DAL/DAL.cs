using MySql.Data.MySqlClient;
using ResultsView.DAL;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;

namespace ResultsView.WebInterface
{
    public class DAL
    {
        private static DAL obj;
        public static DAL Instance
        {
            get
            {
                if (obj == null)
                {
                    obj = new DAL();
                }
                return obj;
            }
        }
        public static DataSet getsetfileheader(string strfilekey, string strcollegeid, int intid, string strprofkey,string strop)
        {
            MySqlConnection conn = new MySqlConnection(ConnectionSource.getConnectionString(""));
            MySqlCommand cmd = new MySqlCommand("spgetset_fileheader", conn);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add("@p_filekey", MySqlDbType.VarChar, 50).Value = strfilekey;
            cmd.Parameters.Add("@p_collegeid", MySqlDbType.VarChar, 50).Value = strcollegeid;
            cmd.Parameters.Add("@p_idlogin", MySqlDbType.Int32).Value = intid;
            cmd.Parameters.Add("@p_profid", MySqlDbType.VarChar, 50).Value = strprofkey;
            cmd.Parameters.Add("@p_strop", MySqlDbType.VarChar, 2).Value = strop;
            DataSet ds = new DataSet();
            try
            {
                conn.Open();
                MySqlDataAdapter da = new MySqlDataAdapter(cmd);
                cmd.Prepare();
                da.Fill(ds);
                conn.Close();
            }
            catch (Exception ex)
            {
                if (conn.State == ConnectionState.Open)
                {
                    conn.Close();
                }
                ds = null;
            }
            return ds;
        }
        public static DataSet getsetprofessor(string struserid, string strpassword, int intid, string strop)
        {
            MySqlConnection conn = new MySqlConnection(ConnectionSource.getConnectionString(""));
            MySqlCommand cmd = new MySqlCommand("spgetset_professor", conn);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add("@p_userid", MySqlDbType.VarChar, 45).Value = struserid;
            cmd.Parameters.Add("@p_password", MySqlDbType.VarChar, 45).Value = strpassword;
            cmd.Parameters.Add("@p_idlogin", MySqlDbType.Int32).Value = intid;
            cmd.Parameters.Add("@p_strop", MySqlDbType.VarChar, 2).Value = strop;
            DataSet ds = new DataSet();
            try
            {
                conn.Open();
                MySqlDataAdapter da = new MySqlDataAdapter(cmd);
                cmd.Prepare();
                da.Fill(ds);
                conn.Close();
            }
            catch (Exception ex)
            {
                if (conn.State == ConnectionState.Open)
                {
                    conn.Close();
                }
                ds = null;
            }
            return ds;
        }

        public static DataSet getsetstudentdata(int stridstudentdata, string strpublickey, int intstudentid,string strgrade, string strop)
        {
            MySqlConnection conn = new MySqlConnection(ConnectionSource.getConnectionString(""));
            MySqlCommand cmd = new MySqlCommand("spgetset_studentdata", conn);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add("@p_idstudentdata", MySqlDbType.Int32).Value = stridstudentdata;
            cmd.Parameters.Add("@p_publickey", MySqlDbType.VarChar, 45).Value = strpublickey;
            cmd.Parameters.Add("@p_studentid", MySqlDbType.Int32).Value = intstudentid;
            cmd.Parameters.Add("@p_grade", MySqlDbType.VarChar, 2).Value = strgrade;
            cmd.Parameters.Add("@p_strop", MySqlDbType.VarChar, 2).Value = strop;
            DataSet ds = new DataSet();
            try
            {
                conn.Open();
                MySqlDataAdapter da = new MySqlDataAdapter(cmd);
                cmd.Prepare();
                da.Fill(ds);
                conn.Close();
            }
            catch (Exception ex)
            {
                if (conn.State == ConnectionState.Open)
                {
                    conn.Close();
                }
                ds = null;
            }
            return ds;
        }
        public static DataSet getsetbillengine(int intmemory, int intstorage, int intcomputetime, int intlogin, string strRequesttype, string strop, string strclienttype)
        {
            MySqlConnection conn = new MySqlConnection(ConnectionSource.getConnectionString(""));
            MySqlCommand cmd = new MySqlCommand("spgetset_billengine", conn);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add("@p_requesttype", MySqlDbType.VarChar, 50).Value = strRequesttype;
            cmd.Parameters.Add("@p_memory", MySqlDbType.Int32).Value = intmemory;
            cmd.Parameters.Add("@p_storage", MySqlDbType.Int32).Value = intstorage;
            cmd.Parameters.Add("@p_computetime", MySqlDbType.Int32).Value = intcomputetime;
            cmd.Parameters.Add("@p_idlogin", MySqlDbType.Int32).Value = intlogin;
            cmd.Parameters.Add("@p_strop", MySqlDbType.VarChar, 2).Value = strop;
            cmd.Parameters.Add("@p_clienttype", MySqlDbType.VarChar, 45).Value = strclienttype;
            cmd.Parameters.Add("@p_datetime", MySqlDbType.DateTime).Value = DateTime.Now.Truncate(TimeSpan.FromMinutes(1)); ;
            DataSet ds = new DataSet();
            try
            {
                conn.Open();
                MySqlDataAdapter da = new MySqlDataAdapter(cmd);
                cmd.Prepare();
                da.Fill(ds);
                conn.Close();
            }
            catch (Exception ex)
            {
                if (conn.State == ConnectionState.Open)
                {
                    conn.Close();
                }
                ds = null;
            }
            return ds;
        }
        
        public static DataSet getloadbalancerdata(string strop)
        {
            MySqlConnection conn = new MySqlConnection(ConnectionSource.getConnectionString(""));
            MySqlCommand cmd = new MySqlCommand("spgetset_loadbalancer", conn);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add("@p_requesttotal", MySqlDbType.Int32).Value = 0;
            cmd.Parameters.Add("@p_algo", MySqlDbType.VarChar,2).Value = "";
            cmd.Parameters.Add("@p_memory", MySqlDbType.Int32).Value = 0;
            cmd.Parameters.Add("@p_storage", MySqlDbType.Int32).Value = 0;
            cmd.Parameters.Add("@p_computetime", MySqlDbType.Int32).Value = 0;
            cmd.Parameters.Add("@p_strop", MySqlDbType.VarChar, 2).Value = strop;
            DataSet ds = new DataSet();
            try
            {
                conn.Open();
                MySqlDataAdapter da = new MySqlDataAdapter(cmd);
                cmd.Prepare();
                da.Fill(ds);
                conn.Close();
            }
            catch (Exception ex)
            {
                if (conn.State == ConnectionState.Open)
                {
                    conn.Close();
                }
                ds = null;
            }
            return ds;
        }
        public static DataSet getzloadbalancerdata(string strop,string algotype,string strzne)
        {
            MySqlConnection conn = new MySqlConnection(ConnectionSource.getConnectionString(""));
            MySqlCommand cmd = new MySqlCommand("spgetset_zloadbalancer", conn);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Add("@p_requesttotal", MySqlDbType.Int32).Value = 0;
            cmd.Parameters.Add("@p_algo", MySqlDbType.VarChar, 2).Value = algotype;
            cmd.Parameters.Add("@p_memory", MySqlDbType.Int32).Value = 0;
            cmd.Parameters.Add("@p_storage", MySqlDbType.Int32).Value = 0;
            cmd.Parameters.Add("@p_computetime", MySqlDbType.Int32).Value = 0;
            cmd.Parameters.Add("@p_zone", MySqlDbType.VarChar, 2).Value = strzne;
            cmd.Parameters.Add("@p_serverid", MySqlDbType.Int32).Value = 0;            
            cmd.Parameters.Add("@p_strop", MySqlDbType.VarChar, 2).Value = strop;
            DataSet ds = new DataSet();
            try
            {
                conn.Open();
                MySqlDataAdapter da = new MySqlDataAdapter(cmd);
                cmd.Prepare();
                da.Fill(ds);
                conn.Close();
            }
            catch (Exception ex)
            {
                if (conn.State == ConnectionState.Open)
                {
                    conn.Close();
                }
                ds = null;
            }
            return ds;
        }


    }
}