<%@ Page Title="" Language="C#" MasterPageFile="~/WebInterface/Master.Master" AutoEventWireup="true" CodeBehind="Signup.aspx.cs" Inherits="ResultsView.WebInterface.Signup" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
     <div class="row">
        <div class="col-lg-6"><h3>Sign In</h3>
            <div class="box">
                
                <div class="row">
                    
                    <div class="col-lg-12">
                        
                        <div class="input-group">
                            <input type="text" id="signinusername" name="signinusername" class="form-control" placeholder="Username" />
                        </div>
                    </div>
                    
                </div>
                <div class="divMargin10px"></div>
                <div class="row">

                   
                    <div class="col-lg-12">
                        <div class="input-group">
                            <input type="password" id="signinpassword" name="signinpassword" class="form-control" placeholder="Password" />
                        </div>

                    </div>
                    
                </div>
                <div class="divMargin10px"></div>
                <div class="row">
                   
                    <div class="col-lg-4">
                        <asp:Button ID="btnSignIn" OnClick="btnSignIn_Click" CssClass="btn btn-primary" runat="server" Text="Sign In" />
                         <%--<button type="button" title="Submit" class="btn btn-primary">Sign in</button>--%>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-6"><h3>Sign Up</h3>
            <div class="box">
                <div class="row">
                    
                    <div class="col-lg-12">
                        <div class="input-group">
                            <input type="text" id="signupusername" name="signupusername" class="form-control" placeholder="Username" />
                        </div>
                    </div>
                </div>
                <div class="divMargin10px"></div>
                <div class="row">
                    <div class="col-lg-12">
                        <div class="input-group">
                            <input type="password" id="signuppassword" name="signuppassword" class="form-control" placeholder="Password" />
                        </div>

                    </div>
                </div>
                <div class="divMargin10px"></div>
                <div class="row">
                    <div class="col-lg-12">
                        <div class="input-group">
                            <input type="password" id="signupconfpassword" name="signupconfpassword" class="form-control" placeholder="Confirm Password" />
                        </div>
                    </div>
                </div>
                <div class="divMargin10px"></div>
                <div class="row">
                    <div class="col-lg-4">
                        <asp:Button ID="btnSignUp" OnClick="btnSignUp_Click" CssClass="btn btn-primary" runat="server" Text="Sign Up" />
                         <%--<button type="button" title="Submit" class="btn btn-primary">Sign up</button>--%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</asp:Content>
