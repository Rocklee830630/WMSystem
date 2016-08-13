using GMS.Account.Contract;
using GMS.Web.Admin.Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using GMS.Framework.Utility;

namespace GMS.Web.Admin.Areas.Store.Controllers
{
    [Permission(EnumBusinessPermission.StoreManage)]
    public class BoundManageController: AdminControllerBase
    {
        public ActionResult Index()
        {
            var items = new List<SelectListItem>()
            {
                (new SelectListItem() {Text ="全部", Value = "1", Selected =false}),
                (new SelectListItem() {Text ="VOGELE订单-上衣", Value = "2", Selected =false}),
                (new SelectListItem() {Text ="VOGELE订单-裤子", Value = "3", Selected =false}),
                (new SelectListItem() {Text ="VOGELE订单-马甲", Value = "4", Selected =false})
            };
            ViewData["OrderType"] = items;
            return View();
        }
    }
}