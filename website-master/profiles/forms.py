from django.forms import ModelForm
from sosokan.models import Ad


class AdsForm(ModelForm):
	class Meta(ModelForm):
		model = Ad
		fields = ('name', 'expires_on', 'chinese', 'language', 'coupon',
			      'description', 'enableEmail', 'enablePhone', 'price',)

	def __init__(self, *args, **kwargs):
		super(AdsForm, self).__init__(*args, **kwargs)
		self.fields['name'].widget.attrs.update({'class' : 'form-control'})
		self.fields['language'].widget.attrs.update({'class' : 'form-control'})
		self.fields['description'].widget.attrs.update({'class':'form-control'})
		self.fields['coupon'].widget.attrs.update({'class' : 'form-control'})
		self.fields['enableEmail'].widget.attrs.update({'class' : 'form-control'})
		self.fields['enablePhone'].widget.attrs.update({'class':'form-control'})
		self.fields['price'].widget.attrs.update({'class':'form-control'})