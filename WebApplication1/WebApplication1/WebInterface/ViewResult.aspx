<%@ Page Title="" Language="C#" MasterPageFile="~/WebInterface/Master.Master" AutoEventWireup="true" CodeBehind="ViewResult.aspx.cs" Inherits="ResultsView.WebInterface.ViewResult" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="row">
       
        <div class="col-lg-12">
            <div class="box">
                <div class="row">
                    <%--<div class="col-lg-5 alignRight">User Name:</div>
                    <div class="col-lg-7 alignLeft">
                        <input type="text" id="username" name="username" />
                    </div>--%>
                    <div class="col-lg-4"></div>
                    <div class="col-lg-4">
                        <div class="input-group input-group-sm">
                            <input type="text" id="username" name="username" class="form-control" placeholder="Student ID" />
                        </div>
                    </div>
                    <div class="col-lg-4"></div>
                </div>
                <div class="divMargin10px"></div>
                <div class="row">

                    <div class="col-lg-4"></div>
                    <div class="col-lg-4">
                        <div class="input-group input-group-sm">
                            <input type="password" id="password" name="password" class="form-control" placeholder="Public Key" />
                        </div>

                    </div>
                    <div class="col-lg-4"></div>


                    <%--<div class="col-lg-5 alignRight">Password:</div>
                    <div class="col-lg-7 alignLeft">
                        <input type="password" id="password" name="password" />
                    </div>--%>
                </div>
                <div class="divMargin10px"></div>
                <div class="row">
                    <div class="col-lg-3"></div>
                    <div class="col-lg-4">
                        <asp:Button ID="btnViewResult" runat="server" OnClick="btnViewResult_Click" Text="View ResultsB"/>
                    </div>
                    <div class="col-lg-5"></div>
                </div>
            </div>
        </div>
        
    </div>
    <div class="row" id="viewResultsRow" runat="server">
        
    </div>

</asp:Content>
