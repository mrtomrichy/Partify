namespace Partify.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Songs1 : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Songs",
                c => new
                    {
                        Id = c.Long(nullable: false, identity: true),
                        SpotifyId = c.String(),
                        PartyId = c.Long(nullable: false),
                        Order = c.Int(nullable: false),
                        Votes = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.Id);
            
        }
        
        public override void Down()
        {
            DropTable("dbo.Songs");
        }
    }
}
