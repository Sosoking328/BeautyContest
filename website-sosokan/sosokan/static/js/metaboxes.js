jQuery(function(){
		var format = jQuery('#post-formats-select input[checked=checked]').val();
		if(format == 'gallery'){
			jQuery('#minelife_gallery_settings') .show();			
		}else if(format == 'video'){
			jQuery('#minelife_video_settings') .show();
		}else if(format == 'audio'){
			jQuery('#minelife_audio_settings') .show();
		}
		
	jQuery('#post-formats-select input').on('click', function(e) {

		var format = jQuery(this).val();
		if(format == 'gallery'){
			jQuery('#minelife_video_settings') .hide();
			jQuery('#minelife_audio_settings') .hide();
			jQuery('#minelife_gallery_settings') .show();			
		}else if(format == 'video'){
			jQuery('#minelife_video_settings') .show();
			jQuery('#minelife_audio_settings') .hide();
			jQuery('#minelife_gallery_settings') .hide();	
		}else if(format == 'audio'){
			jQuery('#minelife_video_settings') .hide();
			jQuery('#minelife_audio_settings') .show();
			jQuery('#minelife_gallery_settings') .hide();	
		}else{
			jQuery('#minelife_video_settings') .hide();
			jQuery('#minelife_audio_settings') .hide();
			jQuery('#minelife_gallery_settings') .hide();	
		}

	});
        
    /* Gallery */
    
    var frame;
    var images = script_data.image_ids;
	
    var selection = loadImages(images);

    jQuery('#gallery_images_upload').on('click', function(e) {
		            e.preventDefault();

            // Set options for 1st frame render
            var options = {
                    title: script_data.label_create,
                    state: 'gallery-edit',
                    frame: 'post',
                    selection: selection
            };

            // Check if frame or gallery already exist
            if( frame || selection ) {
                    options['title'] = script_data.label_edit;
            }

            frame = wp.media(options).open();

            // Tweak views
            frame.menu.get('view').unset('cancel');
            frame.menu.get('view').unset('separateCancel');
            frame.menu.get('view').get('gallery-edit').el.innerHTML = script_data.label_edit;
            frame.content.get('view').sidebar.unset('gallery'); // Hide Gallery Settings in sidebar

            // When we are editing a gallery
            overrideGalleryInsert();
            frame.on( 'toolbar:render:gallery-edit', function() {
            overrideGalleryInsert();
            });

            frame.on( 'content:render:browse', function( browser ) {
                if ( !browser ) return;
                // Hide Gallery Setting in sidebar
                browser.sidebar.on('ready', function(){
                    browser.sidebar.unset('gallery');
                });
                // Hide filter/search as they don't work 
                    browser.toolbar.on('ready', function(){ 
                            if(browser.toolbar.controller._state == 'gallery-library'){ 
                                    browser.toolbar.$el.hide(); 
                            } 
                    }); 
            });

            // All images removed
            frame.state().get('library').on( 'remove', function() {
                var models = frame.state().get('library');
                    if(models.length == 0){
                        selection = false;
                        jQuery.post(ajaxurl, { 
                            ids: '',
                            action: 'minelife_save_images',
                            post_id: script_data.post_id,
                            nonce: script_data.nonce 
                        });
                    }
            });

            // Override insert button
            function overrideGalleryInsert() {
                    frame.toolbar.get('view').set({
                            insert: {
                                    style: 'primary',
                                    text: script_data.label_save,

                                    click: function() {                                            
                                            var models = frame.state().get('library'),
                                                ids = '';

                                            models.each( function( attachment ) {
                                                ids += attachment.id + ','
                                            });

                                            this.el.innerHTML = script_data.label_saving;
                                            
                                            jQuery.ajax({
                                                    type: 'POST',
                                                    url: ajaxurl,
                                                    data: { 
                                                        ids: ids, 
                                                        action: 'minelife_save_images', 
                                                        post_id: script_data.post_id, 
                                                        nonce: script_data.nonce 
                                                    },
                                                    success: function(){
                                                        selection = loadImages(ids);
                                                        jQuery('#gallery_image_ids').val( ids );
                                                        frame.close();
                                                    },
                                                    dataType: 'html'
                                            }).done( function( data ) {
                                                    jQuery('.gallery-thumbs').html( data );
                                            }); 
                                    }
                            }
                    });
            }
        });
					
        // Load images
        function loadImages(images) {
                if( images ){
                    var shortcode = new wp.shortcode({
                        tag:    'gallery',
                        attrs:   { ids: images },
                        type:   'single'
                    });

                    var attachments = wp.media.gallery.attachments( shortcode );

                    var selection = new wp.media.model.Selection( attachments.models, {
                            props:    attachments.props.toJSON(),
                            multiple: true
                    });

                    selection.gallery = attachments.gallery;

                    // Fetch the query's attachments, and then break ties from the
                    // query to allow for sorting.
                    selection.more().done( function() {
                            // Break ties with the query.
                            selection.props.set({ query: false });
                            selection.unmirror();
                            selection.props.unset('orderby');
                    });

                    return selection;
                }
                return false;
        }
});

