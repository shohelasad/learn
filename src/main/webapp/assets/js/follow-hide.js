$(function(){
	$(".follow-hide").on('click', followHide);  
	
	function followHide(event){ 
		event.preventDefault();
		var post = $(this);
		var icon = post.find("span");
		var id = post.attr("id"); 
		
		$.ajax({
			url:post.attr("href"),
			method: "POST",
			beforeSend: function() {
				toggleClassOf(icon);
			},
			error: function(jqXHR) {
				errorPopup(Messages.get('error.occured'), post, "center-popup");
				console.log(jqXHR);
			}
		});
		
		function toggleClassOf(icon){ 
			icon.toggleClass("button-follow-hide");
			icon.toggleClass("button-following-hide");
			
			if(icon.hasClass("button-following-hide")) {
				icon.text("Follow");
				icon.attr("title", Messages.get('validation.follow_post'));
			} 
			
			if(icon.hasClass("button-follow-hide")) { 		
				icon.text("Following");
				$('#channel-' + id).hide();
				//document.getElementById('channel-' + id).style.display = 'none';
				//icon.attr("title", Messages.get('validation.cancel_follow_post'));
			}
		}
	}
});