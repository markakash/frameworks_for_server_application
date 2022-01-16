using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Labo8.Models;

namespace Labo8.Data
{
    public class TheatersContext : DbContext
    {
        public TheatersContext (DbContextOptions<TheatersContext> options)
            : base(options)
        {
        }

        public DbSet<Labo8.Models.NewsMessage> NewsMessage { get; set; }
    }
}
