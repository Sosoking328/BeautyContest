/*-----------------------------------------------------------------------------------*/
/*	Custom Script
/*-----------------------------------------------------------------------------------*/

  jQuery(function() {
			jQuery("#plan_freetext_box").fadeOut();
            jQuery("#plan_freetextt_box").fadeOut();
		
	jQuery("#plan_select_box select").change(function(){
        $this = jQuery(this);
        if( $this.val() == "Free" ){
            jQuery("#plan_ads_box").fadeOut();
            jQuery("#plan_price_box").fadeOut();
            jQuery("#plan_time_box").fadeOut();
            jQuery("#plan_user_box_cancel").fadeOut();
            jQuery("#plan_user_box").fadeOut();
            jQuery("#popular_plan").fadeOut();
			jQuery("#plan_freetext_box").fadeIn();
            jQuery("#plan_freetextt_box").fadeIn();
        }else{
			jQuery("#plan_ads_box").fadeIn();
            jQuery("#plan_price_box").fadeIn();
            jQuery("#plan_time_box").fadeIn();
            jQuery("#plan_free_text_box").fadeIn();
            jQuery("#plan_secure_text_box").fadeIn();
            jQuery("#plan_user_box_cancel").fadeIn();
            jQuery("#plan_user_box").fadeIn();
            jQuery("#popular_plan").fadeIn();
            jQuery("#plan_freetext_box").fadeOut();
            jQuery("#plan_freetextt_box").fadeOut();
			
		}
    });
    jQuery("#plan_select_box select").change();
						
	
	
  });

	