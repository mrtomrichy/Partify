using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.UI.WebControls.WebParts;
using Partify.Models;

namespace Partify.Controllers
{
    [RoutePrefix("api/Party")]
    public class PartyController : ApiController
    {
        //
        // GET: /Party/
        [Route("Create")]
        [System.Web.Http.HttpPost]
        public IHttpActionResult Create(string partyName)
        {
            try
            {
                var code = GeneratePartyKey();
                var newAttendee = new Attendee {PartyCode = code};
                var newParty = new Party {PartyCode = code, Name = partyName};

                using (var db = new PartyContext())
                {
                    db.Attendees.Add(newAttendee);
                    db.Parties.Add(newParty);
                    db.SaveChanges();
                }
                var response = new CreateEventResponse {PartyCode = code, AttendeeId = newAttendee.AttendeeId};
                return Ok(response);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [Route("Add")]
        [System.Web.Http.HttpPost]
        public IHttpActionResult Add(string songId, string partyId)
        {
            var song = new Songs();
            song.SpotifyId = songId;
            song.PartyId = Convert.ToInt64(partyId);
            song.Votes = 0;
            var partyIdd = Convert.ToInt64(partyId);

            using (var db = new PartyContext())
            {
                var currentSongs = db.Songs.Where(x => x.PartyId == partyIdd);
                var highest = 0;
                foreach (Songs songy in currentSongs)
                {
                    if (songy.Order > highest)
                    {
                        highest = songy.Order;
                    }
                }
                song.Order = highest +1;
                db.Songs.Add(song);
                db.SaveChanges();
            }

            return Ok();
        }


        [Route("Test")]
        [System.Web.Http.HttpGet]
        public IHttpActionResult Test(string test)
        {
            return Ok("8=============================================================================================================================================================================================================================================================================================================================================================================================================================DB=====================================================================================================================================================8");
        }

        [Route("Join")]
        [System.Web.Http.HttpPost]
        public IHttpActionResult Join(string partyCode)
        {
            var newAttendee = new Attendee {PartyCode = partyCode};
            using (var db = new PartyContext())
            {
                db.Attendees.Add(newAttendee);
                db.SaveChanges();
            }
            var response = new JoinEventResponse { AttendeeId = newAttendee.AttendeeId };
            return Ok(response);
        }

        [Route("Update")]
        [System.Web.Http.HttpGet]
        public IHttpActionResult Update(string partyCode, string[] songIds)
        {
            return Ok(songIds);
        }


        private string GeneratePartyKey()
        {
            Random rnd = new Random();
            int partyCode = rnd.Next(1000000, 9999999);
            return partyCode.ToString();
        }
    }
}
