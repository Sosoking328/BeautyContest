from django.shortcuts import render, get_object_or_404, redirect
from django.http import HttpResponse, HttpResponseRedirect
from django.core.urlresolvers import reverse
from django.contrib.auth import authenticate, login
from django.views import generic
from .forms import UserForm
from .models import Candidate,Voter
from django.template import loader

class IndexView(generic.ListView):
    template_name=loader.get_template("polls/index.html")
    candidates = Candidate.objects.all()
    context = {'candidates': candidates}
    def get(self,request):
        return render(request,'polls/index.html',self.context)

def index_after(request):
    candidates=Candidate.objects.all()
    context = {'candidates': candidates}
    return render(request,'polls/index_after.html',context)

class ResultsView(generic.ListView):
    candidates=Candidate.objects.all()
    context = {'candidates': candidates}
    def get(self,request):
        return render(request,"polls/results.html",self.context)

def votes(request):
#    candidate = get_object_or_404(Candidate)
    candidates=Candidate.objects.all()
##    user_id = [user.id for user in Voter.objects.filter(poll__id=poll_id)]
    if Voter.objects.filter(user_id=request.user.id).exists():
        return render(request, 'polls/results.html',{'candidates':candidates,'error_message': "Sorry, but you have already voted."})

    try:
        selected_choice = candidates.get(pk=request.POST['contestant'])
    except:
        return render(request,'polls/index_after.html',{'candidates':candidates,'error_message':"Please select a choice now"})
    else:
        # if user haven't voted yet then allow
        selected_choice.vote_count += 1
        selected_choice.save()
        v = Voter(user=request.user,choosen=selected_choice)
        v.save()
        return render(request,"polls/results.html",{'candidates':candidates})
        # else cannot allow to vote again..only redo

    #return HttpResponseRedirect(reverse('polls:results'),args=(selected_choice))

class UserFormView(generic.View):
    form_class = UserForm
    template_name = 'polls/registration_form.html'

    def get(self, request):
        form = self.form_class(None)
        return render(request,self.template_name,{'form':form})

    def post(self,request):
        form = self.form_class(request.POST)
        if form.is_valid():
            user = form.save(commit=False)
            #clean the data
            username = form.cleaned_data['username']
            password = form.cleaned_data['password']
            user.set_password(password)
            user.save()
            # return user objects if crediential are correct
            user = authenticate(username=username, password=password)

            if user is not None:
                if user.is_active:
                    login(request,user)
                    return redirect('polls:index_after')# why use redirect

        return render(request,self.template_name, {'form':form})
