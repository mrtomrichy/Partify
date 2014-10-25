using System;
using System.Data.Entity;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Data.Entity.Infrastructure;

namespace Partify
{
    public class PartyContext : DbContext
    {
        public PartyContext()
            : base("Name=PartyContext")
        {
            this.Configuration.AutoDetectChangesEnabled = true;
        }

        public DbSet<Party> Parties { get; set; }
        public DbSet<Attendee> Attendees { get; set; }
    }

    public class Party
    {
        public long Id { get; set; }
        public string PartyCode { get; set; }
        public string Name { get; set; }
    }

    public class Attendee
    {
        public Attendee()
        {
            AttendeeId = GenerateId();
        }
        public long Id { get; set; }
        public string PartyCode { get; set; }
        public string AttendeeId { get; set; }

        public string GenerateId()
        {
            return Guid.NewGuid().ToString("N");
        }
    }
}