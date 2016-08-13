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
    [Permission(EnumBusinessPermission.StoreManage_Inbound)]
    public class InboundController:AdminControllerBase
    {

        public ActionResult Index()
        { 

            return View();
        }

        //
        // GET: /Cms/Article/Create

        public ActionResult Create()
        {
            var items = new List<SelectListItem>()
            {
                (new SelectListItem() {Text ="VOGELE订单-上衣", Value = "1", Selected =false}),
                (new SelectListItem() {Text ="VOGELE订单-裤子", Value = "2", Selected =false}),
                (new SelectListItem() {Text ="VOGELE订单-马甲", Value = "2", Selected =false}) 
            };
            ViewData["OrderType"] = items;

            var names = new List<SelectListItem>()
            {
                (new SelectListItem() {Text ="大身里布", Value = "1", Selected =false}),
                (new SelectListItem() {Text ="袖里", Value = "2", Selected =false}),
                (new SelectListItem() {Text ="无纺衬", Value = "2", Selected =false})
            };
            ViewData["Materialnames"] = names;
            return View("Edit");
        }
}
}