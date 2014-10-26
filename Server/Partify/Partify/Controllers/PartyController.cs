using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Net;
using System.Web;
using System.Web.Http;
using System.Web.Http.Cors;
using System.Web.Http.Results;
using System.Web.Mvc;
using System.Web.UI.WebControls.WebParts;
using Microsoft.Ajax.Utilities;
using Partify.Models;

namespace Partify.Controllers
{
    [RoutePrefix("api/Party")]
    [EnableCors(origins: "http://partify.apphb.com", headers: "*", methods: "*")]
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
                    if (db.Parties.FirstOrDefault(x => x.Name == partyName) != null)
                        return Unauthorized();
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

        [Route("GetPlaylist")]
        [System.Web.Http.HttpGet]
        public IHttpActionResult GetPlaylist(string partyCodePlaylist)
        {
            var playlist = new List<string>();
            string[] playlists = {};
            using (var db = new PartyContext())
            {
                var partyid = Convert.ToInt64(partyCodePlaylist);
                var songs = db.Songs.Where(x => x.PartyId == partyid);
                if (!songs.Any()) return Ok(playlists);

                if (songs.Any())
                {
                    foreach (var song in songs)
                    {
                        playlist.Add(song.SpotifyId);
                    }
                }
                playlists = playlist.ToArray();
            }
            var response = new GetPlaylistResponse {SpotifyIds = playlists};
            return Ok(response);
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
            return Ok("8===========DB==================8");
        }

        [Route("Join")]
        [System.Web.Http.HttpPost]
        public IHttpActionResult Join(string partyCode)
        {
            var newAttendee = new Attendee();
            using (var db = new PartyContext())
            {
                db.Attendees.Add(newAttendee);
                var party = db.Parties.Where(x => x.PartyCode == partyCode).FirstOrDefault();
                if (party == null)
                {
                    return NotFound();
                }

                var partyName = db.Parties.Where(x => x.PartyCode == partyCode).FirstOrDefault().Name;
                db.SaveChanges();
                var response = new JoinEventResponse {AttendeeId = newAttendee.AttendeeId, PartyName = partyName};

                return Ok(response);
            }
        }

        [Route("UpVote")]
        [System.Web.Http.HttpPost]
        public IHttpActionResult Upvote(string songId, string partyCode)
        {
            using (var db = new PartyContext())
            {
                var party = db.Parties.FirstOrDefault(x => x.PartyCode == partyCode);

                if (party == null)
                    return NotFound();

                var partycode = Convert.ToInt64(partyCode);
                var song = db.Songs.FirstOrDefault(x => x.SpotifyId == songId && x.PartyId == partycode);

                if (song != null) song.Votes++;
               
                //get playlistsongs
                var playlist = db.Songs.Where(x => x.PartyId == partycode).OrderBy(x => x.Votes).ThenBy(x => x.Order);
                //order playlist songs by votes
                var increment = 1;
                var songsList = new List<string>();
                // set the new order
                foreach (var orderedsong in playlist)
                {
                    orderedsong.Order = increment;
                    increment++;
                    songsList.Add(orderedsong.SpotifyId);
                }
                //save the database
                db.SaveChanges();

                var response = new UpVoteResponse {SongIds = songsList.ToArray()};
                return Ok(response);
            }
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
