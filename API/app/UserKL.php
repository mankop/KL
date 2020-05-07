<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class UserKL extends Model
{
    protected $table = 'user';
    protected $primaryKey = 'id';
    protected $fillable = array('name', 'HS');
}
