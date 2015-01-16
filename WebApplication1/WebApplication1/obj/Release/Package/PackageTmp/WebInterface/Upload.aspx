<%@ Page Title="" Language="C#" MasterPageFile="~/WebInterface/Master.Master" AutoEventWireup="true" CodeBehind="Upload.aspx.cs" Inherits="ResultsView.WebInterface.Upload" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <input type="hidden" runat="server" id="hdnID" />
    <input type="file" name="fileUpload" id="fileupload" />
    <asp:Button ID="btnFileUpload" runat="server" OnClick="btnFileUpload_Click" CssClass="btn btn-primary" Text="Upload File" />
</asp:Content>
