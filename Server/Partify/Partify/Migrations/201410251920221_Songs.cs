namespace Partify.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Songs : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Songs",
                c => new
                {
                    Id = c.Long(nullable: false, identity: true),
                    Spotifyid = c.String(nullable: false),
                    Partyid = c.String(nullable: false),
                    Order = c.Long(false),
                    Votes = c.Long(false),
                })
                .PrimaryKey(t => t.Id);
        }
        
        public override void Down()
        {
            DropTable("dbo.Songs");
            DropPrimaryKey("dbo.Songs");
        }
    }
}
