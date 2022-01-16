using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.ComponentModel.DataAnnotations;
using Microsoft.Extensions.Localization;


namespace Labo8.Models
{
    public class NewsMessage
    {
        public int? Id { get; set; }

        [Display(Name = "Title")]
        [Required(ErrorMessage = "Title is required!")]
        public string Title { get; set; }

        [Display(Name = "Message")]
        [Required(ErrorMessage = "Message cannot be empty")]
        public string Message { get; set; }

        [Display(Name = "Date")]
        [Required(ErrorMessage = "Date must be filled")]
        public DateTime Date { get; set; }

    }
}
