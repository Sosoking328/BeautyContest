
from django.test import LiveServerTestCase
import os
import time
from rest_framework.test import APITestCase
from models import Ad
from django_comments.models import Comment
os.environ['DJANGO_LIVE_TEST_SERVER_ADDRESS'] = 'localhost:8082'
from rest_framework.test import APIRequestFactory
from rest_framework import status
from django.contrib.auth.models import User
from django.test.utils import override_settings
from rest_framework.authtoken.models import Token
from models import UserProfile
from django.contrib.contenttypes.models import ContentType



from rest_framework.test import force_authenticate

class AccountTests(APITestCase):

    fixtures = ['initial_data.json']
    
    def setUp(self):
        # setup a user, userprofile and ad for testing, it runs before each test below:
        self.factory = APIRequestFactory()
        self.user = User.objects.create_user(
            username='user@foo.com', email='user@foo.com', password='top_secret')
        self.user.userprofile.legacy_id="123FIREBASEID"
        self.user.userprofile.save()

        self.ad = Ad.objects.create(name='test1', description='test')
        content_type=ContentType.objects.get(app_label="django_comments", model="comment")
        self.comment = Comment.objects.create(object_pk=self.ad.id, content_type=content_type, comment="test setup comment", site_id=1, user_id=self.user.id)

        self.token, created = Token.objects.get_or_create(user=self.user)
        self.token.save()


    def test_create_account(self):
        data = {
            'username': 'testsosokan', 
            'email': 'testing123@gmail.com', 
            'password1': 'secure123', 
            'password2': 'secure123'
            }
        response = self.client.post('/rest-auth/registration/', data, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(User.objects.count(), 2)
        self.assertEqual(User.objects.get(username="testsosokan").username, 'testsosokan')
  
    def test_create_ad(self):
        data = {
            'name': 'test', 
            'language': 'en',
        }
        response = self.client.post('/api/ads/', data, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(Ad.objects.count(), 2)
        self.assertEqual(Ad.objects.get(name="test").name, 'test')
        self.assertEqual(response.data['id'], Ad.objects.get(name="test").id)

    def test_create_ad_comment(self):
        # it will use the user for the token you send
        data = {
            'object_pk': self.ad.id,
            'comment': 'test_comment',
        }

        response = self.client.post('/api/comments/', data, format='json', HTTP_AUTHORIZATION='Token {0}'.format(self.token))
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(Comment.objects.count(), 2)
        self.assertEqual(Comment.objects.get(comment="test_comment").comment, 'test_comment')   
        self.assertTrue('123FIREBASEID' in response.data['firebase_user_id'])     

    def test_update_comment(self): 
        data = {
            'object_pk': self.ad.id,
            'comment': 'test_comment_updated',
            'user_id': self.user.id,
        }
        response = self.client.put('/api/comments/'+str(Comment.objects.get().id)+"/", data, format='json', HTTP_AUTHORIZATION='Token {0}'.format(self.token))
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(Comment.objects.get().comment, 'test_comment_updated')

    def test_delete_comment(self): 
        response = self.client.delete('/api/comments/'+str(Comment.objects.get().id)+"/", format='json', HTTP_AUTHORIZATION='Token {0}'.format(self.token))
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)

    # def test_get_firebase_user_id(self):
        # response = self.client.get('/api/userprofiles/'+str(self.user.id)+"/", format='json', HTTP_AUTHORIZATION='Token {0}'.format(self.token))
        # self.assertTrue('123FIREBASEID' in response.data['legacy_id'])   

    def test_like_a_comment(self):
        response = self.client.get('/api/comments/'+str(self.comment.id)+'/rate/1', format='json', HTTP_AUTHORIZATION='Token {0}'.format(self.token))
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertTrue('1' in response.content)   
        
    def test_unlike_a_comment(self):
        response = self.client.get('/api/comments/'+str(self.comment.id)+'/rate/-1', format='json', HTTP_AUTHORIZATION='Token {0}'.format(self.token))
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertTrue('0' in response.content)   

    def test_ringcaptcha(self):
        data = {
            'phone': '+19929929292', 
            'code': '8829',
        }
        response = self.client.post('/api/phone_login/', data, format='json')
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertTrue('ERROR_INVALID_NUMBER' in response.data['message'])


    def test_facebook_token(self):
        data = {
            'access_token': 'testsosokan', 
            }
        response = self.client.post('/rest-auth/facebook/', data, format='json')
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertTrue('Incorrect value' in response.data['non_field_errors'])

    def test_google_token(self):
        data = {
            'access_token': 'testsosokan', 
            }
        response = self.client.post('/rest-auth/google/', data, format='json')
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertTrue('Incorrect value' in response.data['non_field_errors'])

