<%@ Page Title="" Language="C#" MasterPageFile="~/WebInterface/Master.Master" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="ResultsView.WebInterface.Default" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <h1>Sign in successful!</h1>
    <a id="lnkUpload" class="btn btn-primary"  runat="server">Upload Document</a>
    <a runat="server" class="btn btn-primary" id="lnkBilling">Billing</a>
</asp:Content>
