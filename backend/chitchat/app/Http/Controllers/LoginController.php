<?php
namespace App\Http\Controllers;
use App\User;
use Illuminate\Validation;
use Illuminate\Support\Facades\Redirect;
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;
use Log;

class LoginController extends Controller
{

	public function loginfun(Request $request)
	{

		$email = $request->Input('email');
		$password = $request->Input('password');
		Log::info("User with email ". $email. " trying to login");

	$var = DB::table('users')->where('email',$email)->where('password',$password)->value('name');

	$result['response']=false;

	if(isset($var))
	{
		$result['response']=true;
		$result['name']=$var;
		log::info("Authentication Passed");
	}
	else
	{
		log::info("Authentication Failed");
	}

	return response()->json($result);
	}

	
}