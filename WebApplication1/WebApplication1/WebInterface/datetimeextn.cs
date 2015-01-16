using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ResultsView.WebInterface
{
    public static class datetimeextn
    {
        public static DateTime Truncate(this DateTime dateTime, TimeSpan timeSpan)
        {
            if (timeSpan == TimeSpan.Zero) return dateTime; // Or could throw an ArgumentException
            return dateTime.AddTicks(-(dateTime.Ticks % timeSpan.Ticks));
        }
    }
}