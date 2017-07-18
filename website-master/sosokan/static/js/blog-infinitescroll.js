jQuery(window).load(function() {

    "use strict";
    //Latest Ads 
    jQuery('.latest-ads-holder').each(function(i){
        var $currentPortfolio=jQuery(this);
        var $currentInfinite=$currentPortfolio.find('.jw-infinite-scroll');
        var $currentIsotopeContainer=$currentPortfolio.find('#main').children('.loop-content');
        $currentIsotopeContainer=$currentIsotopeContainer?$currentIsotopeContainer:$currentPortfolio;
        // Infinite
        $currentInfinite.find('a').unbind('click').bind('click',function(e){e.preventDefault();
            var $currentNextLink=jQuery(this);
            if($currentInfinite.attr('data-has-next')==='true'&&$currentNextLink.hasClass('next')){
                var $infiniteURL=$currentNextLink.attr('href');
                $currentInfinite.find('.next').hide();
                $currentInfinite.find('.loading').show();
                jQuery.ajax({
                    type: "POST",
                    url: $infiniteURL,
                    success: function(response){
						//alert(response);
                        var $newElements = jQuery(response).find('#main').children('.loop-content').hasClass('loop-content')?jQuery(response).find('#main').children('.loop-content').html():jQuery(response).find('#main').eq(i).html();
                        var $newURL      = jQuery(response).find('#main').find('.jw-infinite-scroll>a.next').attr('href');
                        var $hasNext     = jQuery(response).find('#main').find('.jw-infinite-scroll').attr('data-has-next');
						if($newElements){
                            //$newElements=jQuery('<div />').append($newElements).find('item').css('opacity','0');
                            if($currentIsotopeContainer.hasClass('loop-content')){
                                $currentIsotopeContainer.append($newElements);
                            }else{
                                $currentInfinite.before($newElements);
                            }
                            if($hasNext==='false'){
                                $currentInfinite.attr('data-has-next','false');
                                $currentInfinite.find('.loading').hide();
                                $currentInfinite.find('.no-more').show();
                            }else{
                                $currentNextLink.attr('href',$newURL);
                                $currentInfinite.find('.loading').hide();
                                $currentInfinite.find('.next').fadeIn();
                            }
                        }else{
                            $currentInfinite.attr('data-has-next','false');
                            $currentInfinite.find('.loading').hide();
                            $currentInfinite.find('.no-more') .show();
                        }
                        setTimeout(function(){
                            $currentIsotopeContainer.children('.ad-box').css('opacity','1');
                            
                        },1000);
                        setTimeout(function(){
                            $currentIsotopeContainer.children('.ad-box').css('opacity','1');
                            
                        },6000);
                    }
                });
            }
        });
    });
	
	
	
	
	//Populer Ads 
    jQuery('.popular-ads-grid-holder').each(function(i){
        var $currentPortfolio=jQuery(this);
        var $currentInfinite=$currentPortfolio.find('.jw-infinite-scroll');
        var $currentIsotopeContainer=$currentPortfolio.find('#catMain3').children('.loop-content2');
        $currentIsotopeContainer=$currentIsotopeContainer?$currentIsotopeContainer:$currentPortfolio;
        // Infinite
        $currentInfinite.find('a').unbind('click').bind('click',function(e){e.preventDefault();
            var $currentNextLink=jQuery(this);
            if($currentInfinite.attr('data-has-next')==='true'&&$currentNextLink.hasClass('next')){
                var $infiniteURL=$currentNextLink.attr('href');
                $currentInfinite.find('.next').hide();
                $currentInfinite.find('.loading').show();
                jQuery.ajax({
                    type: "POST",
                    url: $infiniteURL,
                    success: function(response){
                        var $newElements = jQuery(response).find('#main2').children('.loop-content2').hasClass('loop-content2')?jQuery(response).find('#main2').children('.loop-content2').html():jQuery(response).find('#main2').eq(i).html();
                        var $newURL      = jQuery(response).find('#main2').find('.jw-infinite-scroll>a.next').attr('href');
                        var $hasNext     = jQuery(response).find('#main2').find('.jw-infinite-scroll').attr('data-has-next');
						if($newElements){
                            //$newElements=jQuery('<div />').append($newElements).find('item').css('opacity','0');
                            if($currentIsotopeContainer.hasClass('loop-content2')){
                                $currentIsotopeContainer.append($newElements);
                            }else{
                                $currentInfinite.before($newElements);
                            }
                            if($hasNext==='false'){
                                $currentInfinite.attr('data-has-next','false');
                                $currentInfinite.find('.loading').hide();
                                $currentInfinite.find('.no-more').show();
                            }else{
                                $currentNextLink.attr('href',$newURL);
                                $currentInfinite.find('.loading').hide();
                                $currentInfinite.find('.next').fadeIn();
                            }
                        }else{
                            $currentInfinite.attr('data-has-next','false');
                            $currentInfinite.find('.loading').hide();
                            $currentInfinite.find('.no-more') .show();
                        }
                        setTimeout(function(){
                            $currentIsotopeContainer.children('.ad-box').css('opacity','1');
                            
                        },1000);
                        setTimeout(function(){
                            $currentIsotopeContainer.children('.ad-box').css('opacity','1');
                            
                        },6000);
                    }
                });
            }
        });
    });
	
	
	//Random Ads 
    jQuery('.random-ads-grid-holder').each(function(i){
        var $currentPortfolio=jQuery(this);
        var $currentInfinite=$currentPortfolio.find('.jw-infinite-scroll');
        var $currentIsotopeContainer=$currentPortfolio.find('#main3').children('.loop-content3');
        $currentIsotopeContainer=$currentIsotopeContainer?$currentIsotopeContainer:$currentPortfolio;
        // Infinite
        $currentInfinite.find('a').unbind('click').bind('click',function(e){e.preventDefault();
            var $currentNextLink=jQuery(this);
            if($currentInfinite.attr('data-has-next')==='true'&&$currentNextLink.hasClass('next')){
                var $infiniteURL=$currentNextLink.attr('href');
                $currentInfinite.find('.next').hide();
                $currentInfinite.find('.loading').show();
                jQuery.ajax({
                    type: "POST",
                    url: $infiniteURL,
                    success: function(response){
                        var $newElements = jQuery(response).find('#main3').children('.loop-content3').hasClass('loop-content3')?jQuery(response).find('#main3').children('.loop-content3').html():jQuery(response).find('#main3').eq(i).html();
                        var $newURL      = jQuery(response).find('#main3').find('.jw-infinite-scroll>a.next').attr('href');
                        var $hasNext     = jQuery(response).find('#main3').find('.jw-infinite-scroll').attr('data-has-next');
						if($newElements){
                            //$newElements=jQuery('<div />').append($newElements).find('item').css('opacity','0');
                            if($currentIsotopeContainer.hasClass('loop-content3')){
                                $currentIsotopeContainer.append($newElements);
                            }else{
                                $currentInfinite.before($newElements);
                            }
                            if($hasNext==='false'){
                                $currentInfinite.attr('data-has-next','false');
                                $currentInfinite.find('.loading').hide();
                                $currentInfinite.find('.no-more').show();
                            }else{
                                $currentNextLink.attr('href',$newURL);
                                $currentInfinite.find('.loading').hide();
                                $currentInfinite.find('.next').fadeIn();
                            }
                        }else{
                            $currentInfinite.attr('data-has-next','false');
                            $currentInfinite.find('.loading').hide();
                            $currentInfinite.find('.no-more') .show();
                        }
                        setTimeout(function(){
                            $currentIsotopeContainer.children('.ad-box').css('opacity','1');
                            
                        },1000);
                        setTimeout(function(){
                            $currentIsotopeContainer.children('.ad-box').css('opacity','1');
                            
                        },6000);
                    }
                });
            }
        });
    });

	
	//Premium Ads 
    jQuery('.premiumAds').each(function(i){
        var $currentPortfolio=jQuery(this);
        var $currentInfinite=$currentPortfolio.find('.jw-infinite-scroll');
        var $currentIsotopeContainer=$currentPortfolio.find('#main3').children('.loop-content3');
        $currentIsotopeContainer=$currentIsotopeContainer?$currentIsotopeContainer:$currentPortfolio;
        // Infinite
        $currentInfinite.find('a').unbind('click').bind('click',function(e){e.preventDefault();
            var $currentNextLink=jQuery(this);
            if($currentInfinite.attr('data-has-next')==='true'&&$currentNextLink.hasClass('next')){
                var $infiniteURL=$currentNextLink.attr('href');
                $currentInfinite.find('.next').hide();
                $currentInfinite.find('.loading').show();
                jQuery.ajax({
                    type: "POST",
                    url: $infiniteURL,
                    success: function(response){
                        var $newElements = jQuery(response).find('#main3').children('.loop-content3').hasClass('loop-content3')?jQuery(response).find('#main3').children('.loop-content3').html():jQuery(response).find('#main3').eq(i).html();
                        var $newURL      = jQuery(response).find('#main3').find('.jw-infinite-scroll>a.next').attr('href');
                        var $hasNext     = jQuery(response).find('#main3').find('.jw-infinite-scroll').attr('data-has-next');
						if($newElements){
                            //$newElements=jQuery('<div />').append($newElements).find('item').css('opacity','0');
                            if($currentIsotopeContainer.hasClass('loop-content3')){
                                $currentIsotopeContainer.append($newElements);
                            }else{
                                $currentInfinite.before($newElements);
                            }
                            if($hasNext==='false'){
                                $currentInfinite.attr('data-has-next','false');
                                $currentInfinite.find('.loading').hide();
                                $currentInfinite.find('.no-more').show();
                            }else{
                                $currentNextLink.attr('href',$newURL);
                                $currentInfinite.find('.loading').hide();
                                $currentInfinite.find('.next').fadeIn();
                            }
                        }else{
                            $currentInfinite.attr('data-has-next','false');
                            $currentInfinite.find('.loading').hide();
                            $currentInfinite.find('.no-more') .show();
                        }
                        setTimeout(function(){
                            $currentIsotopeContainer.children('.ad-box').css('opacity','1');
                            
                        },1000);
                        setTimeout(function(){
                            $currentIsotopeContainer.children('.ad-box').css('opacity','1');
                            
                        },6000);
                    }
                });
            }
        });
    });
	
	var ajaxurl = rel_reg_vars.rel_ajax_url;
	jQuery('.load-a').on('click', function(e){
		var counter = jQuery('.cat-wrapper .category-box-outter').length,
			call = jQuery('.cat-wrapper').data('call') ;
		if(counter == call){
			var nom = jQuery('.load-a span').data('nomore') ;
			jQuery(this).html("<i class='fa fa-refresh'></i><span data-nomore='"+nom+"'>"+nom+"</span>");
			jQuery(this).closest('.view-more-btn').fadeOut(800);
		}else{
			var nom = jQuery('.load-a span').data('nomore') ,
				nom2 = jQuery('.load-a span').data('loading') ,
				nom3 = jQuery('.load-a span').data('loaded') ;
			jQuery(this).html("<i class='fa fa-refresh'></i><span data-nomore='"+nom+"' data-loading='"+nom2+"' data-loaded='"+nom3+"'>"+nom2+"</span>");
		}
         jQuery.ajax({
             type: 'POST',
             dataType: 'html',
             url: ajaxurl,
             data: { ccounter: counter },
             success: function(response){
				 jQuery.ajax({
					 type: 'GET',
					 dataType: 'html',
					 url: ajaxurl,
					 data: { action: 'ajax_category_loop',whatever: '1',ccounter: counter },
					 success: function(response){
						 jQuery('.cat-wrapper').append(response);
						var nom = jQuery('.load-a span').data('nomore') ,
							nom2 = jQuery('.load-a span').data('loading') ,
							nom3 = jQuery('.load-a span').data('loaded') ;
						jQuery('.load-a').html("<i class='fa fa-refresh'></i><span data-nomore='"+nom+"' data-loading='"+nom2+"' data-loaded='"+nom3+"'>"+nom3+"</span>");
					 }
				 });
             }
         });
         e.preventDefault();
    });
	jQuery('.searchload-a').on('click', function(e){
		var counter = jQuery('.latest-ads-grid-holder .col-md-3').length,
			call = jQuery('.latest-ads-grid-holder').data('call');
		if(counter == call){
			var nom = jQuery('.searchload-a span').data('nomore') ;
			jQuery(this).html("<i class='fa fa-refresh'></i><span data-nomore='"+nom+"'>"+nom+"</span>");
			jQuery(this).closest('.view-more-btn').fadeOut(800);
		}else{
			var nom = jQuery('.searchload-a span').data('nomore') ,
				nom2 = jQuery('.searchload-a span').data('loading') ,
				nom3 = jQuery('.searchload-a span').data('loaded') ;
			jQuery(this).html("<i class='fa fa-refresh'></i><span data-nomore='"+nom+"' data-loading='"+nom2+"' data-loaded='"+nom3+"'>"+nom2+"</span>");
		}
         jQuery.ajax({
             type: 'POST',
             dataType: 'html',
             url: ajaxurl,
             data: { ccounter: counter },
             success: function(response){
				 jQuery.ajax({
					 type: 'GET',
					 dataType: 'html',
					 url: ajaxurl,
					 data: { action: 'search_posts',whatever: '1',ccounter: counter },
					 success: function(response){
						 jQuery('.latest-ads-grid-holder').append(response);
						var nom = jQuery('.searchload-a span').data('nomore') ,
							nom2 = jQuery('.searchload-a span').data('loading') ,
							nom3 = jQuery('.searchload-a span').data('loaded') ;
						jQuery('.searchload-a').html("<i class='fa fa-refresh'></i><span data-nomore='"+nom+"' data-loading='"+nom2+"' data-loaded='"+nom3+"'>"+nom3+"</span>");
					 }
				 });
             }
         });
         e.preventDefault();
    });
	
});