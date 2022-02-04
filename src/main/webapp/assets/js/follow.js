$(function(){
	$(".follow").on('click', follow); 
	
	function follow(event){ 
		event.preventDefault();
		var post = $(this);
		var icon = post.find("span")
		
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
			icon.toggleClass("button-follow");
			icon.toggleClass("button-following");
			
			if(icon.hasClass("button-following")) {
				icon.text("Follow");
				icon.attr("title", Messages.get('validation.follow_post'));
			} 
			
			if(icon.hasClass("button-follow")) { 
				icon.text("Following");
				icon.attr("title", Messages.get('validation.cancel_follow_post'));
			}
		}
	}
});