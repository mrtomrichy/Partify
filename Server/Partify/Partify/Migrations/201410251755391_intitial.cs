namespace Partify.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class intitial : DbMigration
    {
        public override void Up()
        {
            DropPrimaryKey("dbo.Attendees");
            DropPrimaryKey("dbo.Parties");
            AddColumn("dbo.Attendees", "Id", c => c.Long(nullable: false, identity: true));
            AddColumn("dbo.Parties", "Id", c => c.Long(nullable: false, identity: true));
            AlterColumn("dbo.Attendees", "AttendeeId", c => c.String());
            AddPrimaryKey("dbo.Attendees", "Id");
            AddPrimaryKey("dbo.Parties", "Id");
            DropColumn("dbo.Parties", "PartyId");
        }
        
        public override void Down()
        {
            AddColumn("dbo.Parties", "PartyId", c => c.String(nullable: false, maxLength: 128));
            DropPrimaryKey("dbo.Parties");
            DropPrimaryKey("dbo.Attendees");
            AlterColumn("dbo.Attendees", "AttendeeId", c => c.String(nullable: false, maxLength: 128));
            DropColumn("dbo.Parties", "Id");
            DropColumn("dbo.Attendees", "Id");
            AddPrimaryKey("dbo.Parties", "PartyId");
            AddPrimaryKey("dbo.Attendees", "AttendeeId");
        }
    }
}
