<?php
namespace App\Http\Controllers;
use App\User;
use Illuminate\Validation;
use Illuminate\Support\Facades\Redirect;
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;
use Log;

class Contacts extends Controller
{
    public function resolvecontactsfun(Request $request)
    {
    	$data = $request->getContent();
    	$phone_numbers = json_decode($data)->numbers;

    	for ($i=0; $i < count($phone_numbers); $i++) { 
    		# code...
    		log::info("Phone number reveived ".$phone_numbers[$i]);
    	}
    	$contacts = DB::table('users')
    				->select('name','status','last_seen')
    				->whereIn('phone',$phone_numbers)
    				->get();

    	$result = [];
    	if(count($contacts) > 0)
		{
			$result['contacts']=$contacts;
			log::info("Contacts found");
		}
		else
		{
			log::info("No contacts found");
		}			

    	return response()->json($result);
    }
    
}
