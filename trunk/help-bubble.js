/*
Author: Radical Designs Cooperative, Jack Perkins, Margot Brennan
Date: 6/1/11

To implement this feature, please include this file after the jquery library in the header of all pages. It automatically identifies existing help links with the target of 'helpinright', opening the linked page as an iframe inside of a help bubble div.

The previously targeted frame for displaying the help page can be removed as this feature replaces it.
*/

function getHelp() {
    /* insert our help bubble into the page
    Escaped using http://www.htmlescape.net/ */
    $('body').append('\x3Cdiv id=\"help-bubble\" style=\"display:none;position:absolute;padding:10px;background:#DDF;text-align:right;\"\x3E\n\x3Ciframe id=\"help-contents\" src=\"\" style=\"width:300px;height:200px;border:0px;\"\x3E\n\x3C\x2Fiframe\x3E\n\x3Cbr\x2F\x3E\x3Ca target=\"help-close\"\x3EClose [X]\x3C\x2Fa\x3E\n\x3C\x2Fdiv\x3E');

    /* attach the show function to every help link on the page */
		$('a[target="helpinright"]').click(function() {
			locationIs = $(this).attr('href');
			$('#help-contents').attr('src',locationIs);
			$('#help-bubble').css('display','block'); 
      $('#help-bubble').css('left',$(this).offset().left+20);  
      $('#help-bubble').css('top',$(this).offset().top+10);     
			return false; 
		});

    /* attach the hide function to the body 
		$('body').click(function() {
			$('#help-bubble').css('display','none');
			return false; 
		});
		*/

    /* attach the hide function to the close button on the div */
		$('a[target="help-close"]').click(function() {
			$('#help-bubble').css('display','none');
			return false; 
		});
		

}

$(document).ready(function() {
		getHelp();	
});
